package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ShoppingCartVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.ShoppingCartRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShoppingCartServiceContract {

    ProductVO addToShoppingCart(ShoppingCartRequest shoppingCartRequest);

    ShoppingCartVO updateShoppingCart(ShoppingCartVO shoppingCartVO);

    ShoppingCartVO findShoppingCartById(ShoppingCartVO shoppingCartVO);

    Page<ProductEntity> findShoppingCartProducts(Pageable pageable);
}
