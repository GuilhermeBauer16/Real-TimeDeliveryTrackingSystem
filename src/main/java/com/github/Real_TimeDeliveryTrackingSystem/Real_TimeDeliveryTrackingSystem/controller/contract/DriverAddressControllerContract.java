package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Interface for managing driver addresses.
 * <p>
 * Provides endpoints for creating, updating, retrieving, and deleting addresses associated with drivers.
 * Includes Swagger/OpenAPI annotations for API documentation.
 */

public interface DriverAddressControllerContract {

    /**
     * Creates a new address for a driver.
     * <p>Endpoint: <code>POST</code></p>
     *
     * @param addressVO the address details encapsulated in an {@link AddressVO} object
     * @return a {@link ResponseEntity} containing the created {@link AddressVO}
     */

    @PostMapping()
    @Operation(summary = "Register a new address",
            description = "Creates a new address and returns the created address.",
            tags = "Driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = AddressVO.class))),
            @ApiResponse(responseCode = "400", description = "Will throw Invalid Address",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Will Throw Driver Not Found, Field Not Found, Address Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<AddressVO> create(@RequestBody AddressVO addressVO);

    /**
     * Updates an existing address of a customer.
     *
     * <p>Endpoint: <code>PUT</code></p>
     *
     * @param addressVO the updated address details encapsulated in an {@link AddressVO} object
     * @return a {@link ResponseEntity} containing the updated {@link AddressVO} object
     */
    @PutMapping()
    @Operation(summary = "Update an address",
            description = "Update an address and returns the updated address.",
            tags = "Driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = AddressVO.class))),
            @ApiResponse(responseCode = "400", description = "Will throw Invalid Address",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Will Throw Driver Not Found, Field Not Found, Address Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<AddressVO> update(@RequestBody AddressVO addressVO);

    /**
     * Retrieves an address by its ID.
     * <p>Endpoint: <code>GET/{id}</code></p>
     *
     * @param addressId the ID of the address
     * @return a {@link ResponseEntity} containing the retrieved {@link AddressVO}
     */
    @GetMapping(value = "/{id}")
    @Operation(summary = "find an address by id",
            description = "find an address by id and returns the recovered address.",
            tags = "Driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = AddressVO.class))),

            @ApiResponse(responseCode = "404", description = "Will Throw Driver Not Found, Address Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<AddressVO> findById(@PathVariable("id") String addressId);

    /**
     * Retrieves all addresses associated with drivers.
     * <p>Endpoint: <code>GET</code></p>
     *
     * @param pageable pagination details
     * @return a {@link ResponseEntity} containing a {@link Page} of {@link AddressVO} objects
     */
    @GetMapping()
    @Operation(summary = "find all addresses",
            description = "find all addresses of the customer.",
            tags = "Driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = AddressVO.class))),

            @ApiResponse(responseCode = "404", description = "Will Throw Driver Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<Page<AddressVO>> findAll(Pageable pageable);

    /**
     * Deletes an address by its ID.
     * <p>Endpoint: <code>DELETE/{id}</code></p>
     *
     * @param addressId the ID of the address to delete
     * @return a {@link ResponseEntity} with no content
     */
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete an address",
            description = "Delete an address associated with a customer",
            tags = "Driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation, will return no content",
                    content = @Content(schema = @Schema(implementation = AddressVO.class))),

            @ApiResponse(responseCode = "404", description = "Will Throw Driver Not Found, Address Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<Void> delete(@PathVariable("id") String addressId);


}
