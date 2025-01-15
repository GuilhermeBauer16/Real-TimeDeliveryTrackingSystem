package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product.ProductNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.FieldNotFound;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory.ProductFactory;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.ProductRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.ProductServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements ProductServiceContract {

    private static final String PRODUCT_NOT_FOUND = "This product was not found.";

    private final ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository productRepository) {

        this.repository = productRepository;
    }

    @Override
    public ProductVO createProduct(ProductVO productVO) {

        ValidatorUtils.checkObjectIsNullOrThrowException(productVO, PRODUCT_NOT_FOUND, ProductNotFoundException.class);
        ProductEntity productEntity = ProductFactory.create(productVO.getName(), productVO.getDescription(), productVO.getPrice());
        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(productEntity, PRODUCT_NOT_FOUND, FieldNotFound.class);
        ProductEntity savedProduct = repository.save(productEntity);
        return BuildMapper.parseObject(new ProductVO(), savedProduct);
    }

    @Override
    public ProductVO updateProduct(ProductVO productVO) {

        ProductEntity productEntity = repository.findById(productVO.getId())
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        ProductEntity updatedProduct = ValidatorUtils.updateFieldIfNotNull(productEntity, productVO, PRODUCT_NOT_FOUND, FieldNotFound.class);
        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(updatedProduct, PRODUCT_NOT_FOUND, FieldNotFound.class);
        repository.save(updatedProduct);
        
        return BuildMapper.parseObject(new ProductVO(), updatedProduct);
    }

    @Override
    public ProductVO findProductById(String productId) {

        ProductEntity productEntity = repository.findById(productId).orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
        return BuildMapper.parseObject(new ProductVO(), productEntity);
    }

    @Override
    public Page<ProductVO> findProductsByName(String name,Pageable pageable) {

        Page<ProductEntity> products = repository.findByProductName(name, pageable);
        List<ProductVO> productVOS = products.getContent().stream().map(productEntity ->
                BuildMapper.parseObject(new ProductVO(), productEntity)).toList();



        return new PageImpl<>(productVOS,pageable,products.getTotalElements());
    }

    @Override
    public Page<ProductVO> findAllProducts(Pageable pageable) {

        Page<ProductEntity> products = repository.findAll(pageable);
        List<ProductVO> productVOS = products.getContent().stream().map(productEntity ->
                BuildMapper.parseObject(new ProductVO(), productEntity)).toList();

        return new PageImpl<>(productVOS, pageable, products.getTotalElements());
    }

    @Override
    public void deleteProduct(String productId) {

        ProductEntity productEntity = repository.findById(productId).orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
        repository.delete(productEntity);

    }
}
