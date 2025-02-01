package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.ShoppingCartRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.ShoppingCartResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShoppingCartServiceContract {

    ProductVO addToShoppingCart(ShoppingCartRequest shoppingCartRequest);

    TemporaryProductVO findShoppingCartTemporaryProductById(String id);

    void deleteShoppingCartTemporaryProductById(String id);

    ShoppingCartResponse findShoppingCart();

    Page<TemporaryProductEntity> findShoppingCartProducts(Pageable pageable);

    void deleteShoppingCart();
}
