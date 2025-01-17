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

/**
 * Interface defining the contract for product-related operations in the customer controller.
 * This contract provides methods to find products by ID, name, and retrieve all products,
 * all with appropriate API documentation using Swagger annotations.
 */
public interface ProductCustomerControllerContract {

    /**
     * Finds a product by its ID.
     *
     * @param id the ID of the product to find
     * @return the found product wrapped in a {@link ResponseEntity}
     */
    @GetMapping(value = "/{id}")
    @Operation(summary = "find product by it's id",
            description = "Find product by it's id and returns the founded product.",
            tags = "Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = ProductVO.class))),
            @ApiResponse(responseCode = "404", description = "Will throw Product Not found Exception",
                    content = @Content),

            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<ProductVO> findProductById(@PathVariable("id") String id);

    /**
     * Finds products by name.
     *
     * @param name the name of the products to find
     * @param pageable pagination information
     * @return a {@link Page} containing the found products wrapped in a {@link ResponseEntity}
     */
    @GetMapping("findByName/{name}")
    @Operation(summary = "find products by name",
            description = "Find product by name id and returns the founded products.",
            tags = "Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<Page<ProductVO>> findProductsByName(@PathVariable("name") String name, Pageable pageable);


    /**
     * Finds all products.
     *
     * @param pageable pagination information
     * @return a {@link Page} containing all the found products wrapped in a {@link ResponseEntity}
     */
    @GetMapping()
    @Operation(summary = "find all products",
            description = "Find all products and returns the founded products.",
            tags = "Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<Page<ProductVO>> findAllProduct(Pageable pageable);


}
