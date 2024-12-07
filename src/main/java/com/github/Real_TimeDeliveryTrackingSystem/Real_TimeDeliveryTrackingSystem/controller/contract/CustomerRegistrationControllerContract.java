package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.CustomerRegistrationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * REST API contract for customer registration operations.
 *
 * <p>This interface defines an endpoint for registering new customers, handling the creation of customer records,
 * and returning the appropriate response based on the operation's success or failure.</p>
 *
 * @see CustomerVO
 * @see CustomerRegistrationResponse
 */

public interface CustomerRegistrationControllerContract {

    /**
     * Registers a new customer.
     *
     * <p>Endpoint: <code>POST</code> with content type <code>application/json</code>.</p>
     * <p>This method creates a new customer record using the provided {@link CustomerVO} object and returns a
     * {@link CustomerRegistrationResponse} object with the details of the registered customer.</p>
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register a new customer",
            description = "Creates a new customer and returns the created customer.",
            tags = "Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = CustomerRegistrationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Will throw Invalid Customer, Invalid Address",
                    content = @Content),

            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<CustomerRegistrationResponse> registerCustomer(@RequestBody CustomerVO customerVO);
}
