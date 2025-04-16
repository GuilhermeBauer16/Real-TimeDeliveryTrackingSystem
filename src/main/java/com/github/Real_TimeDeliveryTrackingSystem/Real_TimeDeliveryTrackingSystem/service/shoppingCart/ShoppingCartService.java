package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.shoppingCart;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ShoppingCartEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product.ProductNotAssociatedWithTheCustomerException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.shoppingCart.ShoppingCartNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory.ShoppingCartFactory;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.ShoppingCartRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.ShoppingCartRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.ShoppingCartResponse;
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
    private final TemporaryProductService temporaryProductService;

    private static final String SHOPPING_CART_NOT_FOUND_MESSAGE = "The ShoppingCart was not found!";
    private static final String PRODUCT_NOT_ASSOCIATED_WITH_USER_MESSAGE = "This product is not associated with this Customer!";

    @Autowired
    public ShoppingCartService(CustomerService customerService, ProductService productService, ShoppingCartRepository repository, TemporaryProductService temporaryProductService) {
        this.customerService = customerService;
        this.productService = productService;
        this.repository = repository;
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


            ShoppingCartEntity shoppingCartEntity = shoppingCartByCustomerEmail.get();


            List<TemporaryProductEntity> products = shoppingCartEntity.getTemporaryProducts();

            addProductIfWasNotAdded(productVO.getId(), shoppingCartEntity);

            if (verifyIfProductAlreadyAddToCustomerList(productVO.getId(), products)) {

                TemporaryProductVO temporaryProductById = temporaryProductService.findTemporaryProductById(productVO.getId());

                if (shoppingCartRequest.getQuantity() < temporaryProductById.getQuantity()) {

                    ProductVO updatedProduct = updateProduct(shoppingCartRequest.getQuantity(), productVO);
                    double updatedTotalPrice = shoppingCartEntity.getTotalPrice() - updatedProduct.getPrice();
                    TemporaryProductVO temporaryProductVO = BuildMapper.parseObject(new TemporaryProductVO(), updatedProduct);
                    temporaryProductService.updateTemporaryProduct(temporaryProductVO);

                    shoppingCartEntity.setTotalPrice(shoppingCartEntity.getTotalPrice() - updatedTotalPrice);

                }

                if (shoppingCartRequest.getQuantity() > temporaryProductById.getQuantity()) {

                    int updatedQuantity = shoppingCartRequest.getQuantity() - temporaryProductById.getQuantity();
                    double updatedPrice = updatedQuantity * productVO.getPrice();
                    ProductVO updatedProduct = updateProduct(shoppingCartRequest.getQuantity(), productVO);
                    TemporaryProductVO temporaryProductVO = BuildMapper.parseObject(new TemporaryProductVO(), updatedProduct);
                    temporaryProductService.updateTemporaryProduct(temporaryProductVO);
                    shoppingCartEntity.setTotalPrice(shoppingCartEntity.getTotalPrice() + updatedPrice);


                }


            } else {

                ProductVO updatedProduct = updateProduct(shoppingCartRequest.getQuantity(), productVO);
                TemporaryProductEntity temporaryProductEntity = saveTemporaryProduct(updatedProduct);
                shoppingCartEntity.setTotalPrice(shoppingCartEntity.getTotalPrice() + updatedProduct.getPrice());
                shoppingCartEntity.getTemporaryProducts().add(temporaryProductEntity);






            }


            repository.save(shoppingCartEntity);


            return productVO;

        }

        List<ProductEntity> productEntities = new ArrayList<>();
        List<TemporaryProductEntity> tempProducts = new ArrayList<>();


        productEntities.add(BuildMapper.parseObject(new ProductEntity(), productVO));

        ProductVO updatedProduct = updateProduct(shoppingCartRequest.getQuantity(), productVO);

        tempProducts.add(saveTemporaryProduct(updatedProduct));

        ShoppingCartEntity shoppingCartModel = ShoppingCartFactory.create(customerEntity, productEntities, productVO.getPrice(), tempProducts);
        repository.save(shoppingCartModel);

        return updatedProduct;
    }



    @Override
    public TemporaryProductVO findShoppingCartTemporaryProductById(String id) {

        ShoppingCartEntity shoppingCartEntity = repository.findShoppingCartByCustomerEmail(retrieveUserEmail())
                .orElseThrow(() -> new ShoppingCartNotFoundException(SHOPPING_CART_NOT_FOUND_MESSAGE));

        if (!verifyIfProductAlreadyAddToCustomerList(id, shoppingCartEntity.getTemporaryProducts())
        ) {
            throw new ProductNotAssociatedWithTheCustomerException(PRODUCT_NOT_ASSOCIATED_WITH_USER_MESSAGE);
        }

        return temporaryProductService.findTemporaryProductById(id);
    }

    @Override
    @Transactional
    public void deleteShoppingCartTemporaryProductById(String id) {

        ShoppingCartEntity shoppingCartEntity = repository.findShoppingCartByCustomerEmail(retrieveUserEmail())
                .orElseThrow(() -> new ShoppingCartNotFoundException(SHOPPING_CART_NOT_FOUND_MESSAGE));

        if (!verifyIfProductAlreadyAddToCustomerList(id, shoppingCartEntity.getTemporaryProducts())) {

            throw new ProductNotAssociatedWithTheCustomerException(PRODUCT_NOT_ASSOCIATED_WITH_USER_MESSAGE);
        }

        TemporaryProductVO temporaryProductById = temporaryProductService.findTemporaryProductById(id);
        shoppingCartEntity.setTotalPrice(shoppingCartEntity.getTotalPrice() - temporaryProductById.getPrice());
        repository.save(shoppingCartEntity);

        temporaryProductService.deleteTemporaryProduct(id);


    }

    @Override
    public ShoppingCartResponse findShoppingCart() {

        ShoppingCartEntity shoppingCartEntity = repository.findShoppingCartByCustomerEmail(retrieveUserEmail())
                .orElseThrow(() -> new ShoppingCartNotFoundException(SHOPPING_CART_NOT_FOUND_MESSAGE));

        return BuildMapper.parseObject(new ShoppingCartResponse(), shoppingCartEntity);
    }


    @Override
    public Page<TemporaryProductVO> findShoppingCartProducts(Pageable pageable) {

        ShoppingCartEntity shoppingCartModel = repository.findShoppingCartByCustomerEmail(retrieveUserEmail())
                .orElseThrow(() -> new ShoppingCartNotFoundException(SHOPPING_CART_NOT_FOUND_MESSAGE));

        Page<TemporaryProductEntity> allTemporaryProductsByShoppingCart = repository.
                findAllTemporaryProductsByShoppingCart(shoppingCartModel.getId(), pageable);
        List<TemporaryProductVO> temporaryProductVOS = allTemporaryProductsByShoppingCart.getContent()
                .stream().map(temporaryProductEntity -> BuildMapper.parseObject(new TemporaryProductVO(), temporaryProductEntity)).toList();

        return new PageImpl<>(temporaryProductVOS, pageable, allTemporaryProductsByShoppingCart.getTotalElements());
    }

    @Override
    public void deleteShoppingCart(String email) {

        ShoppingCartEntity shoppingCartEntity = repository.findShoppingCartByCustomerEmail(email)
                .orElseThrow(() -> new ShoppingCartNotFoundException(SHOPPING_CART_NOT_FOUND_MESSAGE));

        repository.delete(shoppingCartEntity);

    }

    private static ProductVO updateProduct(int quantity, ProductVO productVO) {
        productVO.setQuantity(quantity);
        productVO.setPrice(productVO.getPrice() * productVO.getQuantity());
        return productVO;
    }

    private TemporaryProductEntity saveTemporaryProduct(ProductVO productVO) {

        TemporaryProductVO temporaryProductVO = BuildMapper.parseObject(new TemporaryProductVO(), productVO);
        TemporaryProductVO savedTemporaryProduct = temporaryProductService.createTemporaryProduct(temporaryProductVO);
        return BuildMapper.parseObject(new TemporaryProductEntity(), savedTemporaryProduct);
    }

    private String retrieveUserEmail() {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUsername();

    }


    private boolean verifyIfProductAlreadyAddToCustomerList(String productId, List<TemporaryProductEntity> productEntities) {


        for (TemporaryProductEntity productEntity : productEntities) {

            if (productEntity.getId().equals(productId)) {

                return true;
            }
        }

        return false;
    }

    private void addProductIfWasNotAdded(String productId, ShoppingCartEntity shoppingCartEntity) {

        boolean productExists = shoppingCartEntity.getProducts()
                .stream()
                .anyMatch(product -> product.getId().equals(productId));

        if (!productExists) {
            ProductVO productById = productService.findProductById(productId);

            shoppingCartEntity.getProducts().add(BuildMapper.parseObject(new ProductEntity(), productById));
        }
    }


}
