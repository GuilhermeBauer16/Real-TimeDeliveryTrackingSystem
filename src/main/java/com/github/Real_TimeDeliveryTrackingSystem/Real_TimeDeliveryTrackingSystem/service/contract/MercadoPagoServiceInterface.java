package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

public interface MercadoPagoServiceInterface {

    String createPreference() throws MPApiException, MPException;

    void handlerWithApprovedPayment(String id, String topic);


}
