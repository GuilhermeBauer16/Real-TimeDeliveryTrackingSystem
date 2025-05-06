package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Contract interface for handling Mercado Pago payment notifications.
 * <p>
 * This interface defines the endpoint that will be triggered when Mercado Pago sends a notification
 * about a payment event. Upon receiving the notification, it processes the event and may trigger
 * an email to the payer.
 */
public interface MercadoPagoNotificationControllerContract {

    /**
     * Handles incoming payment notifications from Mercado Pago.
     * <p>
     * This method is invoked when Mercado Pago sends a webhook notification. It receives the notification
     * parameters such as the payment ID and topic, processes the event accordingly (e.g., verifying the payment),
     * and may trigger additional actions such as sending a confirmation email to the payer.
     * <p>
     * Possible responses:
     * <ul>
     *     <li><b>200 OK:</b> The notification was successfully handled.</li>
     *     <li><b>400 Bad Request:</b> Thrown if there is a problem with the request, such as missing data
     *         or an empty product list.</li>
     *     <li><b>500 Internal Server Error:</b> Thrown in case of an unexpected server error.</li>
     * </ul>
     *
     * @param id    the identifier of the payment or resource related to the notification
     * @param topic the topic of the notification (e.g., "payment", "merchant_order")
     * @return a {@link ResponseEntity} with no content indicating the result of processing
     */
    @PostMapping
    @Operation(summary = "handler with Payment Notification",
            description = "handler with Payment Notification using the Mercado Pago API, and " +
                    "a mail to the payer",
            tags = "MercadoPago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Will throw Mercado Pago Exception or" +
                    "Empty Product List Exception",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<Void> handleWithNotificationPayment(@RequestParam("id") String id,
                                                       @RequestParam("topic") String topic);
}
