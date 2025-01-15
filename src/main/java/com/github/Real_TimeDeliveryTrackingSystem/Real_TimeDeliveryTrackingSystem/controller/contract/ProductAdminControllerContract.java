package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.CustomerRegistrationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ProductAdminControllerContract {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
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
    ResponseEntity<ProductVO> registerProduct(@RequestBody ProductVO productVO);

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
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
    ResponseEntity<ProductVO> updateProduct(@RequestBody ProductVO productVO);

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Register a new customer",
            description = "Creates a new customer and returns the created customer.",
            tags = "Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = CustomerRegistrationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Will throw Invalid Customer, Invalid Address",
                    content = @Content),

            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<Void> deleteProduct(@PathVariable("id") String id);
}
