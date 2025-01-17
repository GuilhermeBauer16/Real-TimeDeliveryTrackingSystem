package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * REST API contract for managing customers addresses.
 *
 * <p>This interface defines the endpoints for handling customer-related operations,
 * including,managing customer addresses, and retrieving address information.</p>
 *
 * @see DeleteMapping
 * @see PostMapping
 * @see PutMapping
 * @see GetMapping
 */

public interface CustomerAddressControllerContract {

    /**
     * Adds a new address to a customer.
     *
     * <p>Endpoint: <code>POST</code></p>
     *
     * @param addressVO the address details encapsulated in an {@link AddressVO} object
     * @return a {@link ResponseEntity} containing the added {@link AddressVO} object
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register a new address",
            description = "Creates a new address and returns the created address.",
            tags = "Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = AddressVO.class))),
            @ApiResponse(responseCode = "400", description = "Will throw Invalid Address",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Will Throw Customer Not Found, Field Not Found, Address Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<AddressVO> addAddressToCustomer(@RequestBody AddressVO addressVO);

    /**
     * Updates an existing address of a customer.
     *
     * <p>Endpoint: <code>PUT </code></p>
     *
     * @param addressVO the updated address details encapsulated in an {@link AddressVO} object
     * @return a {@link ResponseEntity} containing the updated {@link AddressVO} object
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an address",
            description = "Update an address and returns the updated address.",
            tags = "Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = AddressVO.class))),
            @ApiResponse(responseCode = "400", description = "Will throw Invalid Address",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Will Throw Customer Not Found, Field Not Found, Address Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<AddressVO> updateAddressOfACustomer(@RequestBody AddressVO addressVO);

    /**
     * Finds a specific address of a customer by its unique identifier.
     *
     * <p>Endpoint: <code>GET /{id}</code></p>
     *
     * <p> <code>addressId</code>: the unique identifier of the address</p>
     * @return a {@link ResponseEntity} containing the {@link AddressVO} object, or a not-found status if the address does not exist
     */
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "find an address by id",
            description = "find an address by id and returns the recovered address.",
            tags = "Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = AddressVO.class))),

            @ApiResponse(responseCode = "404", description = "Will Throw Customer Not Found, Address Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<AddressVO> findAddressOfACustomerByItsId(@PathVariable("id")String AddressId);

    /**
     * Retrieves a paginated list of all addresses associated with a customer.
     *
     * <p>Endpoint: <code>GET</code></p>
     *
     * @param pageable the pagination and sorting information encapsulated in a {@link Pageable} object
     * @return a {@link ResponseEntity} containing a {@link Page} of {@link AddressVO} objects
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "find all addresses",
            description = "find all addresses of the customer.",
            tags = "Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = AddressVO.class))),

            @ApiResponse(responseCode = "404", description = "Will Throw Customer Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<Page<AddressVO>> findAllAddressesOfACustomer(Pageable pageable);

    /**
     * Deletes a specific address of a customer by its unique identifier.
     *
     * <p>Endpoint: <code>DELETE /{id}</code></p>
     *
     * @param addressId the unique identifier of the address to delete
     * @return a {@link ResponseEntity} with no content
     */
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete an address",
            description = "Delete an address associated with a customer",
            tags = "Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation, will return no content",
                    content = @Content(schema = @Schema(implementation = AddressVO.class))),

            @ApiResponse(responseCode = "404", description = "Will Throw Customer Not Found, Address Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<Void> deleteAddressOfACustomer(@PathVariable("id") String addressId);
}
