package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Contract interface for handling Mercado Pago payment operations.
 * <p>
 * This interface defines the endpoint for creating a payment preference using the Mercado Pago API.
 * Implementations of this interface should handle the creation of a payment preference and return
 * a URL that the client can use to complete the payment.
 */
public interface MercadoPagoPaymentControllerContract {

    /**
     * Creates a payment preference using the Mercado Pago API.
     * <p>
     * This endpoint receives a {@link PaymentRequest} containing the necessary payment details,
     * processes the request with the Mercado Pago API, and returns a payment link.
     * <p>
     * Possible responses:
     * <ul>
     *     <li><b>200 OK:</b> Returns a string with the payment link.</li>
     *     <li><b>400 Bad Request:</b> Thrown if there is an issue with the Mercado Pago API or the input data.</li>
     *     <li><b>500 Internal Server Error:</b> Thrown in case of an unexpected server error.</li>
     * </ul>
     *
     * @param paymentRequest the payment request containing product details and payer information
     * @return a {@link ResponseEntity} containing a string with the payment link
     * @throws MPException if an error occurs when communicating with Mercado Pago
     * @throws MPApiException if an API-level error occurs during the Mercado Pago operation
     */
    @PostMapping("/create")
    @Operation(summary = "Do a Payment",
            description = "Do a Payment using the mercado Pago API and return an String with the payment link!",
            tags = "MercadoPago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Will throw Mercado Pago Exception",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<String> createPreference(@RequestBody PaymentRequest paymentRequest) throws MPException, MPApiException;
}
