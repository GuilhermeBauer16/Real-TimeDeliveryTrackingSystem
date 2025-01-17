package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract.ProductCustomerControllerContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer/product")
public class ProductCustomerController implements ProductCustomerControllerContract {


    private final ProductService service;

    @Autowired
    public ProductCustomerController(ProductService productService) {
        this.service = productService;
    }

    @Override
    public ResponseEntity<ProductVO> findProductById(String id) {
        ProductVO product = service.findProductById(id);
        return ResponseEntity.ok(product);
    }

    @Override
    public ResponseEntity<Page<ProductVO>> findProductsByName(String name, Pageable pageable) {
        Page<ProductVO> productsByName = service.findProductsByName(name, pageable);
        return ResponseEntity.ok(productsByName);
    }

    @Override
    public ResponseEntity<Page<ProductVO>> findAllProduct(Pageable pageable) {
        Page<ProductVO> allProducts = service.findAllProducts(pageable);
        return ResponseEntity.ok(allProducts);
    }
}
