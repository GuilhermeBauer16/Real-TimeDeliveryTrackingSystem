package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ShoppingCartEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ShoppingCartVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory.ShoppingCartFactory;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.ShoppingCartRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.TemporaryProductRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.ShoppingCartRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.ShoppingCartServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.customer.CustomerService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.ProductService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.TemporaryProductService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.QuantityUtils;
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
    private final TemporaryProductRepository temporaryProductRepository;
    private final TemporaryProductService temporaryProductService;

    @Autowired
    public ShoppingCartService(CustomerService customerService, ProductService productService, ShoppingCartRepository repository, TemporaryProductRepository temporaryProductRepository, TemporaryProductService temporaryProductService) {
        this.customerService = customerService;
        this.productService = productService;
        this.repository = repository;
        this.temporaryProductRepository = temporaryProductRepository;
        this.temporaryProductService = temporaryProductService;
    }

    @Override
    @Transactional
    public ProductVO addToShoppingCart(ShoppingCartRequest shoppingCartRequest) {


        CustomerVO customerByEmail = customerService.findCustomerByEmail(retrieveUserEmail());
        CustomerEntity customerEntity = BuildMapper.parseObject(new CustomerEntity(), customerByEmail);

        ProductVO productVO = productService.findProductById(shoppingCartRequest.getProductId());
        QuantityUtils.verifyIfQuantityRequiredIsHigherThanTheAvailable(shoppingCartRequest.getQuantity(), productVO.getQuantity());
        QuantityUtils.checkIfQuantityIsHigherThanOne(shoppingCartRequest.getQuantity());

        Optional<ShoppingCartEntity> shoppingCartByCustomerEmail = repository.findShoppingCartByCustomerEmail(retrieveUserEmail());


        if (shoppingCartByCustomerEmail.isPresent()) {

            ShoppingCartEntity shoppingCartModel = shoppingCartByCustomerEmail.get();
            Double totalPrice = shoppingCartModel.getTotalPrice();


            List<TemporaryProductEntity> products = shoppingCartModel.getTemporaryProducts();


            if (verifyIfProductAlreadyAdd(productVO.getId(), products)) {

                TemporaryProductEntity temporaryProductEntity = temporaryProductService.findProductById(productVO.getId());

                if (shoppingCartRequest.getQuantity() < temporaryProductEntity.getQuantity()) {

                    productVO.setQuantity(shoppingCartRequest.getQuantity());
                    productVO.setPrice(productVO.getPrice() * productVO.getQuantity());
                    double updatedTotalPrice = shoppingCartModel.getTotalPrice() - productVO.getPrice();
                    TemporaryProductEntity updatedTemporaryProductEntity = BuildMapper.parseObject(new TemporaryProductEntity(), productVO);
                    TemporaryProductEntity savedProductEntity = temporaryProductRepository.save(updatedTemporaryProductEntity);
                    shoppingCartModel.setTotalPrice(shoppingCartModel.getTotalPrice() - updatedTotalPrice);

                }

                if (shoppingCartRequest.getQuantity() > temporaryProductEntity.getQuantity()) {
                    int updatedQuantity = shoppingCartRequest.getQuantity() - temporaryProductEntity.getQuantity();
                    double updatedPrice = updatedQuantity * productVO.getPrice();
                    productVO.setQuantity(shoppingCartRequest.getQuantity());
                    productVO.setPrice(productVO.getPrice() * productVO.getQuantity());
                    shoppingCartModel.setTotalPrice(shoppingCartModel.getTotalPrice() + updatedPrice);

                }


                List<TemporaryProductEntity> updatedtempList = changeQuantityInProductAlreadyAdd(temporaryProductEntity, products);
                shoppingCartModel.setTemporaryProducts(new ArrayList<>(updatedtempList));


            } else {

                productVO.setQuantity(shoppingCartRequest.getQuantity());
                productVO.setPrice(productVO.getPrice() * productVO.getQuantity());
                TemporaryProductEntity updatedTemporaryProductEntity = BuildMapper.parseObject(new TemporaryProductEntity(), productVO);
                TemporaryProductEntity savedProductEntity = temporaryProductRepository.save(updatedTemporaryProductEntity);
                products.add(savedProductEntity);
                shoppingCartModel.setTemporaryProducts(new ArrayList<>(products));
                shoppingCartModel.setTotalPrice(shoppingCartModel.getTotalPrice() + productVO.getPrice());


            }


            ShoppingCartEntity savedProduct = repository.save(shoppingCartModel);


            return productVO;


        }

        List<ProductEntity> productEntities = new ArrayList<>();
        List<TemporaryProductEntity> tempProducts = new ArrayList<>();


        productEntities.add(BuildMapper.parseObject(new ProductEntity(), productVO));

        productVO.setQuantity(shoppingCartRequest.getQuantity());
        productVO.setPrice(productVO.getPrice() * shoppingCartRequest.getQuantity());
        Double totalPrice = productVO.getPrice();

        TemporaryProductEntity temporaryProductEntity = BuildMapper.parseObject(new TemporaryProductEntity(), productVO);
        TemporaryProductEntity savedProductEntity = temporaryProductService.createProduct(temporaryProductEntity);

        tempProducts.add(savedProductEntity);
        ShoppingCartEntity shoppingCartModel = ShoppingCartFactory.create(customerEntity, productEntities, totalPrice, tempProducts);
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
    public Page<TemporaryProductEntity> findShoppingCartProducts(Pageable pageable) {

        ShoppingCartEntity shoppingCartModel = repository.findShoppingCartByCustomerEmail(retrieveUserEmail())
                .orElseThrow(() -> new RuntimeException("This shopping cart does not exist"));

        Page<TemporaryProductEntity> allTemporaryProductsByShoppingCart = repository.findAllTemporaryProductsByShoppingCart(shoppingCartModel.getId(), pageable);

        return new PageImpl<>(allTemporaryProductsByShoppingCart.getContent(), pageable, allTemporaryProductsByShoppingCart.getTotalElements());
    }

    @Override
    public void deleteShoppingCart() {

        ShoppingCartEntity shoppingCartEntity = repository.findShoppingCartByCustomerEmail(retrieveUserEmail())
                .orElseThrow(() -> new RuntimeException("This shopping cart does not exist"));

        repository.delete(shoppingCartEntity);

    }

    private String retrieveUserEmail() {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUsername();

    }


    private boolean verifyIfProductAlreadyAdd(String productId, List<TemporaryProductEntity> productEntities) {


        for (TemporaryProductEntity productEntity : productEntities) {

            if (productEntity.getId().equals(productId)) {

                return true;
            }
        }

        return false;
    }

    private List<TemporaryProductEntity> changeQuantityInProductAlreadyAdd(TemporaryProductEntity actualProductEntity, List<TemporaryProductEntity> temporaryProductEntities) {


        for (TemporaryProductEntity productEntity : temporaryProductEntities) {

            if (productEntity.getId().equals(actualProductEntity.getId())) {
                productEntity.setQuantity(actualProductEntity.getQuantity());
                productEntity.setPrice(actualProductEntity.getPrice());
                return temporaryProductEntities;
            }
        }

        return temporaryProductEntities;
    }


}
