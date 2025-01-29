package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract.ShoppingCartControllerContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.ShoppingCartRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController implements ShoppingCartControllerContract {

    private final ShoppingCartService service;

    @Autowired
    public ShoppingCartController(ShoppingCartService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<ProductVO> addProductsToShoppingCart(ShoppingCartRequest shoppingCartRequest)
            throws InstantiationException, IllegalAccessException, NoSuchFieldException {

        ProductVO productVO = service.addToShoppingCart(shoppingCartRequest);

        return ResponseEntity.ok(productVO);
    }

    @Override
    public ResponseEntity<Page<ProductEntity>> findShoppingCartProducts(Pageable pageable) {

        Page<ProductEntity> shoppingCartProducts = service.findShoppingCartProducts(pageable);
        return ResponseEntity.ok(shoppingCartProducts);
    }
}
