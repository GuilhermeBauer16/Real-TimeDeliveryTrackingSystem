package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
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

/**
 * Interface defining the contract for product management operations in the admin controller.
 * This contract provides methods for registering, updating, and deleting products,
 * all with appropriate API documentation using Swagger annotations.
 */
public interface ProductAdminControllerContract {

    /**
     * Registers a new product based on the provided product details.
     *
     * @param productVO the product details to be registered
     * @return the registered product wrapped in a {@link ResponseEntity}
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register a new product",
            description = "Creates a new product and returns the created product.",
            tags = "Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = ProductVO.class))),
            @ApiResponse(responseCode = "400", description = "Will throw Invalid Product Exception",
                    content = @Content),

            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<ProductVO> registerProduct(@RequestBody ProductVO productVO);

    /**
     * Updates an existing product based on the provided product details.
     *
     * @param productVO the product details to update
     * @return the updated product wrapped in a {@link ResponseEntity}
     */

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a product",
            description = "Update a product and returns the updated customer.",
            tags = "Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = ProductVO.class))),
            @ApiResponse(responseCode = "404", description = "Will throw Product Not found Exception",
                    content = @Content),

            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<ProductVO> updateProduct(@RequestBody ProductVO productVO);

    /**
     * Deletes an existing product based on the provided product ID.
     *
     * @param id the ID of the product to delete
     * @return a {@link ResponseEntity} with no content upon successful deletion
     */
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a product",
            description = "Delete a product and returns no content.",
            tags = "Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Will throw Product Not found Exception",
                    content = @Content),

            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<Void> deleteProduct(@PathVariable("id") String id);
}
