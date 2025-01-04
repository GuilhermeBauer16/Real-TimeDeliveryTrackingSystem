package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.DriverVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.DriverRegistrationResponse;
import com.google.i18n.phonenumbers.NumberParseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.VehicleService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.AddressService;

/**
 * Interface for the Driver Registration Controller.
 * <p>
 * Defines the contract for registering a new driver in the system.
 * Includes API documentation for Swagger/OpenAPI integration.
 */

public interface DriverRegistrationControllerContract {


    /**
     * Registers a new driver in the system.
     * <p>
     * This endpoint allows creating a new driver and returns the created driver's details
     * upon successful registration.
     *
     * @param driverVO the {@link DriverVO} object containing the driver's details
     * @return a {@link ResponseEntity} containing the {@link DriverRegistrationResponse}
     * @throws NumberParseException if the driver's phone number is invalid
     * @see DriverVO
     * @see DriverRegistrationResponse
     * @see VehicleService
     * @see AddressService
     */

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register a new driver",
            description = "Creates a new driver and returns the created driver.",
            tags = "Driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = DriverRegistrationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Will throw Invalid Invalid, Invalid Address, Invalid Vehicle",
                    content = @Content),

            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<DriverRegistrationResponse> registerCustomer(@RequestBody DriverVO driverVO) throws NumberParseException;
}
