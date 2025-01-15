package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.CustomerRegistrationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductCustomerControllerContract {

    @GetMapping(value = "/{id}")
    @Operation(summary = "Register a new customer",
            description = "Creates a new customer and returns the created customer.",
            tags = "Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = CustomerRegistrationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Will throw Invalid Customer, Invalid Address",
                    content = @Content),

            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<ProductVO> findProductById(@PathVariable("id") String id);

    @GetMapping("findByName/{name}")
    @Operation(summary = "Register a new customer",
            description = "Creates a new customer and returns the created customer.",
            tags = "Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = CustomerRegistrationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Will throw Invalid Customer, Invalid Address",
                    content = @Content),

            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<Page<ProductVO>> findProductsByName(@PathVariable("name") String name, Pageable pageable);

    @GetMapping()
    @Operation(summary = "Register a new customer",
            description = "Creates a new customer and returns the created customer.",
            tags = "Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = CustomerRegistrationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Will throw Invalid Customer, Invalid Address",
                    content = @Content),

            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<Page<ProductVO>> findAllProduct(Pageable pageable);


}
