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

/**
 * REST API contract for managing customers and their addresses.
 *
 * <p>This interface defines the endpoints for handling customer-related operations,
 * including deleting a customer, managing customer addresses, and retrieving address information.</p>
 *
 * @see DeleteMapping
 * @see PostMapping
 * @see PutMapping
 * @see GetMapping
 */

public interface DriverControllerContract {

    /**
     * Deletes a customer by their password.
     *
     * <p>Endpoint: <code>DELETE </code></p>
     *
     * @param passwordDTO is necessary to be informed to delete the customer.
     * @return a {@link ResponseEntity} with no content
     */
    @DeleteMapping
    @Operation(summary = "Delete a customer",
            description = "Delete a customer and return no content",
            tags = "Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation, will return no content",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Will throw Invalid Password",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Will Throw Customer Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<Void> delete(@RequestBody PasswordDTO passwordDTO);



    
}
