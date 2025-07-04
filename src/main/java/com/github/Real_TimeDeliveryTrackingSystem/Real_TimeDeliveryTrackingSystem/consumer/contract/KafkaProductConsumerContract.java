package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.consumer.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentProcessedRequest;

public interface KafkaProductConsumerContract {

    void listenProductUpdate(PaymentProcessedRequest paymentProcessedRequest);
}
