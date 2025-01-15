package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductServiceContract {

    ProductVO createProduct(ProductVO productVO);

    ProductVO updateProduct(ProductVO productVO);

    ProductVO findProductById(String productId);

    Page<ProductVO> findProductsByName(String name, Pageable pageable);

    Page<ProductVO> findAllProducts(Pageable pageable);

    void deleteProduct(String productId);

}
