package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract.ProductAdminControllerContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductAdminController implements ProductAdminControllerContract {

    private final ProductService service;

    @Autowired
    public ProductAdminController(ProductService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<ProductVO> registerProduct(ProductVO productVO) {
        ProductVO product = service.createProduct(productVO);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ProductVO> updateProduct(ProductVO productVO) {
        ProductVO product = service.updateProduct(productVO);
        return ResponseEntity.ok(product);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(String id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
