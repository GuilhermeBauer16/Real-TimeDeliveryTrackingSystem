package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ShoppingCartVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.driver.DuplicatedLicensePlateException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.vehicle.InvalidLicensePlateException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.vehicle.LicensePlateNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.vehicle.VehicleNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.ShoppingCartRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.ShoppingCartResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * VehicleControllerContract interface defines the API endpoints for managing vehicles
 * in the Real-Time Delivery Tracking System application.
 * It provides methods to create, update, retrieve, and delete vehicles.
 */

public interface ShoppingCartControllerContract {


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProductVO> addProductsToShoppingCart(@RequestBody ShoppingCartRequest shoppingCartRequest) throws InstantiationException, IllegalAccessException, NoSuchFieldException;

    @GetMapping(value="/findProduct/{id}")
    ResponseEntity<TemporaryProductVO> findShoppingCartTemporaryProductById(@PathVariable("id") String id);

    @GetMapping
    ResponseEntity<Page<TemporaryProductEntity>> findShoppingCartTemporaryProducts(Pageable pageable);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteShoppingCartTemporaryProductById(@PathVariable("id") String id);

    @GetMapping(value= "/findShoppingCart", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ShoppingCartResponse> findShoppingCart();

    @DeleteMapping
    ResponseEntity<Void> deleteShoppingCart();



}


