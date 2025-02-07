package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product.InvalidProductException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product.ProductNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.FieldNotFound;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory.TemporaryProductFactory;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.TemporaryProductRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.TemporaryProductServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.PriceUtils;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.QuantityUtils;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TemporaryProductVO createTemporaryProduct(TemporaryProductVO temporaryProductVO) {

        ValidatorUtils.checkObjectIsNullOrThrowException(temporaryProductVO, INVALID_PRODUCT_MESSAGE, InvalidProductException.class);
        QuantityUtils.checkIfQuantityIsHigherThanOne(temporaryProductVO.getQuantity());
        PriceUtils.checkIfPriceIsHigherThanZero(temporaryProductVO.getPrice());
        TemporaryProductEntity productEntity = TemporaryProductFactory.create(temporaryProductVO.getId(), temporaryProductVO.getName(), temporaryProductVO.getDescription(), temporaryProductVO.getPrice(), temporaryProductVO.getQuantity());
        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(productEntity, INVALID_PRODUCT_MESSAGE, FieldNotFound.class);
        TemporaryProductEntity savedTemporaryProduct = repository.save(productEntity);
        return BuildMapper.parseObject(new TemporaryProductVO(), savedTemporaryProduct);
    }

    @Override
    public TemporaryProductVO updateTemporaryProduct(TemporaryProductVO temporaryProductVO) {

        TemporaryProductEntity temporaryProductEntity = repository.findById(temporaryProductVO.getId())
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        TemporaryProductEntity updatedProduct = ValidatorUtils.updateFieldIfNotNull(temporaryProductEntity, temporaryProductVO, PRODUCT_NOT_FOUND, FieldNotFound.class);
        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(updatedProduct, PRODUCT_NOT_FOUND, FieldNotFound.class);
        QuantityUtils.checkIfQuantityIsHigherThanOne(temporaryProductVO.getQuantity());
        PriceUtils.checkIfPriceIsHigherThanZero(temporaryProductVO.getPrice());
        TemporaryProductEntity savedUpdatedProduct = repository.save(updatedProduct);

        return BuildMapper.parseObject(new TemporaryProductVO(), savedUpdatedProduct);

    }

    @Override
    public TemporaryProductVO findTemporaryProductById(String id) {

        TemporaryProductEntity temporaryProductEntity = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        return BuildMapper.parseObject(new TemporaryProductVO(), temporaryProductEntity);
    }

    @Override
    public Page<ProductVO> findAllProducts(Pageable pageable) {

        Page<TemporaryProductEntity> products = repository.findAll(pageable);
        List<ProductVO> productVOS = products.getContent().stream().map(productEntity ->
                BuildMapper.parseObject(new ProductVO(), productEntity)).toList();

        return new PageImpl<>(productVOS, pageable, products.getTotalElements());
    }

    @Override
    public void deleteTemporaryProduct(String id) {

        TemporaryProductEntity temporaryProductEntity = repository
                .findById(id).orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
        repository.delete(temporaryProductEntity);

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

}
