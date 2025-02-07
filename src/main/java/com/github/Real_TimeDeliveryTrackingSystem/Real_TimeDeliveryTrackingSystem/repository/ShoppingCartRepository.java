package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ShoppingCartEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCartEntity,String> {

    @Query("SELECT sc FROM ShoppingCartEntity sc WHERE sc.customer.user.email =:email")
    Optional<ShoppingCartEntity> findShoppingCartByCustomerEmail(@Param("email")String email);

    @Query("SELECT p FROM ShoppingCartEntity sc JOIN sc.products p WHERE sc.id =:id ")
    Page<ProductEntity> findAllProductsByShoppingCart(@Param("id") String shoppingCartId, Pageable pageable);

    @Query("SELECT p FROM ShoppingCartEntity sc JOIN sc.temporaryProducts p WHERE sc.id =:id ")
    Page<TemporaryProductEntity> findAllTemporaryProductsByShoppingCart(@Param("id") String shoppingCartId, Pageable pageable);

}
