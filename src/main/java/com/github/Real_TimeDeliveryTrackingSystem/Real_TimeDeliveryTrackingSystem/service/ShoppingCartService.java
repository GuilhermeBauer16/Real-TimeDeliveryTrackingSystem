package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ShoppingCartEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ShoppingCartVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory.ShoppingCartFactory;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.ShoppingCartRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.ShoppingCartRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.ShoppingCartServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.customer.CustomerService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.ProductService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.QuantityUtils;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService implements ShoppingCartServiceContract {

    private final CustomerService customerService;
    private final ProductService productService;
    private final ShoppingCartRepository repository;

    @Autowired
    public ShoppingCartService(CustomerService customerService, ProductService productService, ShoppingCartRepository repository) {
        this.customerService = customerService;
        this.productService = productService;
        this.repository = repository;
    }

    @Override
    @Transactional
    public ProductVO addToShoppingCart(ShoppingCartRequest shoppingCartRequest) {

        CustomerVO customerByEmail = customerService.findCustomerByEmail(retrieveUserEmail());
        CustomerEntity customerEntity = BuildMapper.parseObject(new CustomerEntity(), customerByEmail);
        List<ProductEntity> productEntities = new ArrayList<>();
        List<ProductEntity> tempProducts = new ArrayList<>();
        ProductVO productVO = productService.findProductById(shoppingCartRequest.getProductId());
        QuantityUtils.verifyIfQuantityRequiredIsHigherThanTheAvailable(shoppingCartRequest.getQuantity(), productVO.getQuantity());
        QuantityUtils.checkIfQuantityIsHigherThanOne(shoppingCartRequest.getQuantity());

        Optional<ShoppingCartEntity> shoppingCartByCustomerEmail = repository.findShoppingCartByCustomerEmail(retrieveUserEmail());

        if(shoppingCartByCustomerEmail.isPresent()) {

            ShoppingCartEntity shoppingCartModel = shoppingCartByCustomerEmail.get();
            Double totalPrice = shoppingCartModel.getTotalPrice();

            Double finalPrice = productVO.getPrice() * shoppingCartRequest.getQuantity();
            totalPrice += finalPrice;
            productVO.setQuantity(shoppingCartRequest.getQuantity());
            productVO.setPrice(productVO.getPrice() * productVO.getQuantity());
            ProductEntity productEntity = BuildMapper.parseObject(new ProductEntity(), productVO);
            shoppingCartModel.setTotalPrice(totalPrice);
            List<ProductEntity> products = shoppingCartModel.getTemporaryProducts();


            List<ProductEntity> temporaryProducts = shoppingCartModel.getTemporaryProducts();

            if(verifyIfProductAlreadyAdd(productEntity.getId(), products)) {

                List<ProductEntity> updatedtempList = changeQuantityInProductAlreadyAdd(productEntity, products);
                shoppingCartModel.setTemporaryProducts(new ArrayList<>(updatedtempList));
            }else {

                ProductEntity updatedProduct = ValidatorUtils.updateFieldIfNotNull(productEntity, productVO, "Error", RuntimeException.class);
                temporaryProducts.add(updatedProduct);
                shoppingCartModel.setTemporaryProducts(new ArrayList<>(temporaryProducts));
            }






            ShoppingCartEntity savedProduct = repository.save(shoppingCartModel);

//            repository.delete(shoppingCartModel);

            return productVO;


        }


        Double totalPrice = productVO.getPrice() * shoppingCartRequest.getQuantity();
        productVO.setQuantity(shoppingCartRequest.getQuantity());
        productVO.setPrice(productVO.getPrice() * shoppingCartRequest.getQuantity());
        ProductEntity updatedProduct = ValidatorUtils.updateFieldIfNotNull(new ProductEntity(), productVO, "Error", RuntimeException.class);
        tempProducts.add(updatedProduct);

        ShoppingCartEntity shoppingCartModel = ShoppingCartFactory.create(customerEntity, productEntities, totalPrice,tempProducts);
        repository.save(shoppingCartModel);

        return productVO;
    }

    @Override
    public ShoppingCartVO updateShoppingCart(ShoppingCartVO shoppingCartVO) {
        return null;
    }

    @Override
    public ShoppingCartVO findShoppingCartById(ShoppingCartVO shoppingCartVO) {
        return null;
    }

    @Override
    public Page<ProductEntity> findShoppingCartProducts(Pageable pageable) {

        ShoppingCartEntity shoppingCartModel = repository.findShoppingCartByCustomerEmail(retrieveUserEmail())
                .orElseThrow(() -> new RuntimeException("This shopping cart does not exist"));

        // Retrieve the list of temporary products
        List<ProductEntity> temporaryProducts = shoppingCartModel.getTemporaryProducts();

        // Calculate the start and end indices for the requested page
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        int endItem = Math.min(startItem + pageSize, temporaryProducts.size());

        // Create a sublist for the current page, if within bounds
        List<ProductEntity> paginatedList;
        if (startItem >= temporaryProducts.size()) {
            paginatedList = List.of(); // Return an empty list if the start index exceeds the list size
        } else {
            paginatedList = temporaryProducts.subList(startItem, endItem);
        }

        // Return a PageImpl with the sublist, pageable, and total size
        return new PageImpl<>(paginatedList, pageable, temporaryProducts.size());
    }

    private String retrieveUserEmail() {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUsername();

    }


    private boolean verifyIfProductAlreadyAdd(String productId, List<ProductEntity> productEntities) {



        for(ProductEntity productEntity : productEntities) {

            if(productEntity.getId().equals(productId)) {

                return true;
            }
        }

        return false;
    }

    private List<ProductEntity> changeQuantityInProductAlreadyAdd(ProductEntity actualProductEntity, List<ProductEntity> productEntities) {



        for(ProductEntity productEntity : productEntities) {

            if(productEntity.getId().equals(actualProductEntity.getId())) {
                productEntity.setQuantity(actualProductEntity.getQuantity());
                productEntity.setPrice(actualProductEntity.getPrice());
                return productEntities;
            }
        }

        return productEntities;
    }


}
