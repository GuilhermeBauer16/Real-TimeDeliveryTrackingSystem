package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    private static final String KAFKA_EMAIL_VERIFICATION_TOPIC = "email-verification-topic";
    private static final String KAFKA_PAYMENT_APPROVED_TOPIC = "payment-approved-topic";


    @Bean
    public NewTopic emailVerificationTopic() {
        return TopicBuilder.name(KAFKA_EMAIL_VERIFICATION_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic paymentApprovedTopic() {
        return TopicBuilder.name(KAFKA_PAYMENT_APPROVED_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();

    }
}
