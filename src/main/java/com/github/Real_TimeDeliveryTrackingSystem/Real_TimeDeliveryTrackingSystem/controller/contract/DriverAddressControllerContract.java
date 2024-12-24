package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.dto.PasswordDTO;
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



public interface DriverAddressControllerContract {


    @PostMapping()
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
    ResponseEntity<AddressVO> create(@RequestBody AddressVO addressVO);

    /**
     * Updates an existing address of a customer.
     *
     * <p>Endpoint: <code>PUT /updateAddress</code></p>
     *
     * @param addressVO the updated address details encapsulated in an {@link AddressVO} object
     * @return a {@link ResponseEntity} containing the updated {@link AddressVO} object
     */
    @PutMapping()
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
    ResponseEntity<AddressVO> update(@RequestBody AddressVO addressVO);


    @GetMapping(value = "/{id}")
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
    ResponseEntity<AddressVO> findById(@PathVariable("id")String addressId);


    @GetMapping()
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
    ResponseEntity<Page<AddressVO>> findAll(Pageable pageable);


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
    ResponseEntity<Void> delete(@PathVariable("id") String addressId);

    
}
