package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.producer;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentProcessedRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProductProducer {


    private final KafkaTemplate<String, PaymentProcessedRequest> productKafkaTemplate;
    private static final String KAFKA_PRODUCT_TOPIC = "product-topic";

    @Autowired
    public KafkaProductProducer(KafkaTemplate<String, PaymentProcessedRequest> productKafkaTemplate) {

        this.productKafkaTemplate = productKafkaTemplate;
    }

    public void sendProductMessage(PaymentProcessedRequest paymentProcessedRequest) {
        productKafkaTemplate.send(KAFKA_PRODUCT_TOPIC, paymentProcessedRequest);
    }

}
