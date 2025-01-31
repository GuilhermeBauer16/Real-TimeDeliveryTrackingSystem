package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product.InvalidProductException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product.ProductNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.FieldNotFound;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory.ProductFactory;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory.TemporaryProductFactory;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.ProductRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.TemporaryProductRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.ProductServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.TemporaryProductServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.PriceUtils;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.QuantityUtils;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemporaryProductService implements TemporaryProductServiceContract {

    private static final String PRODUCT_NOT_FOUND = "This product was not found.";
    private static final String INVALID_PRODUCT_MESSAGE = "This product is invalid, please verify the fields and try again.";

    private final TemporaryProductRepository repository;

    @Autowired
    public TemporaryProductService(TemporaryProductRepository temporaryProductRepository) {

        this.repository = temporaryProductRepository;
    }

    @Override
    public TemporaryProductEntity createProduct(TemporaryProductEntity productVO) {

        ValidatorUtils.checkObjectIsNullOrThrowException(productVO, INVALID_PRODUCT_MESSAGE, InvalidProductException.class);
        QuantityUtils.checkIfQuantityIsHigherThanOne(productVO.getQuantity());
        PriceUtils.checkIfPriceIsHigherThanZero(productVO.getPrice());
        TemporaryProductEntity productEntity = TemporaryProductFactory.create(productVO.getId(),productVO.getName(), productVO.getDescription(), productVO.getPrice(),productVO.getQuantity());
        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(productEntity, INVALID_PRODUCT_MESSAGE, FieldNotFound.class);
        TemporaryProductEntity savedProduct = repository.save(productEntity);
        return savedProduct;
    }

    @Override
    public TemporaryProductEntity updateProduct(TemporaryProductEntity productVO) {

        TemporaryProductEntity productEntity = repository.findById(productVO.getId())
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        TemporaryProductEntity updatedProduct = ValidatorUtils.updateFieldIfNotNull(productEntity, productVO, PRODUCT_NOT_FOUND, FieldNotFound.class);
        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(updatedProduct, PRODUCT_NOT_FOUND, FieldNotFound.class);
        QuantityUtils.checkIfQuantityIsHigherThanOne(productVO.getQuantity());
        PriceUtils.checkIfPriceIsHigherThanZero(productVO.getPrice());
        repository.save(updatedProduct);
        
        return updatedProduct;
    }

    @Override
    public TemporaryProductEntity findProductById(String productId) {

        TemporaryProductEntity productEntity = repository.findById(productId).orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
        return productEntity;
    }

//    @Override
//    public Page<ProductVO> findProductsByName(String name,Pageable pageable) {
//
//        Page<ProductEntity> products = repository.findByProductName(name, pageable);
//        List<ProductVO> productVOS = products.getContent().stream().map(productEntity ->
//                BuildMapper.parseObject(new ProductVO(), productEntity)).toList();
//
//
//
//        return new PageImpl<>(productVOS,pageable,products.getTotalElements());
//    }

    @Override
    public Page<ProductVO> findAllProducts(Pageable pageable) {

        Page<TemporaryProductEntity> products = repository.findAll(pageable);
        List<ProductVO> productVOS = products.getContent().stream().map(productEntity ->
                BuildMapper.parseObject(new ProductVO(), productEntity)).toList();

        return new PageImpl<>(productVOS, pageable, products.getTotalElements());
    }

    @Override
    public void deleteProduct(String productId) {

        TemporaryProductEntity productEntity = repository.findById(productId).orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
        repository.delete(productEntity);

    }
}
