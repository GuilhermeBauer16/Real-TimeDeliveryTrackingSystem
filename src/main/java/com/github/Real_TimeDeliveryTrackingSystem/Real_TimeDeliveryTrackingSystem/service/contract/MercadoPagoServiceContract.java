package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

/**
 * Interface for handling MercadoPago-related operations.
 */
public interface MercadoPagoServiceContract {

    /**
     * Creates a new MercadoPago payment preference.
     *
     * @return a {@link String} representing the generated preference ID or URL.
     * @throws MPApiException if an error occurs while interacting with the MercadoPago API.
     * @throws MPException if a general MercadoPago error occurs.
     */
    String createPreference() throws MPApiException, MPException;

    /**
     * Handles the logic when a payment is approved by MercadoPago.
     *
     * @param id the unique identifier of the payment.
     * @param topic the topic of the notification, usually "payment".
     */
    void handlerWithApprovedPayment(String id, String topic);


}
