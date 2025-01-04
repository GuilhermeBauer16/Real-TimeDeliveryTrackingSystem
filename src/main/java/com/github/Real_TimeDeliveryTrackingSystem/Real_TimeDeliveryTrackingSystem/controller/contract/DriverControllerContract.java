package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.dto.PasswordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Interface for the Driver Controller.
 * <p>
 * Defines the contract for a driver operations.
 * Includes API documentation for Swagger/OpenAPI integration.
 *
 * @see DeleteMapping
 */
public interface DriverControllerContract {

    /**
     * Deletes a driver by their password.
     *
     * <p>Endpoint: <code>DELETE </code></p>
     *
     * @param passwordDTO is necessary to be informed to delete the driver.
     * @return a {@link ResponseEntity} with no content
     */
    @DeleteMapping
    @Operation(summary = "Delete a driver",
            description = "Delete a driver and return no content",
            tags = "Driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation, will return no content",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Will throw Invalid Password",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Will Throw Driver Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<Void> delete(@RequestBody PasswordDTO passwordDTO);


}
