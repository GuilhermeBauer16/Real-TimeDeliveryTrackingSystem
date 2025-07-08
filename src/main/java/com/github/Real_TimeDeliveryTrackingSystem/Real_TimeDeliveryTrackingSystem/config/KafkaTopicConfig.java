package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    private static final String KAFKA_EMAIL_VERIFICATION_TOPIC = "email-verification-topic";
    private static final String KAFKA_PAYMENT_APPROVED_TOPIC = "payment-approved-topic";
    private static final String KAFKA_PRODUCT_TOPIC = "product-topic";
    private static final String RETENTION_MS_30_MINUTES = "1800000";
    private static final String RETENTION_MS_CONFIG = "retention.ms";


    @Bean
    public NewTopic emailVerificationTopic() {

        Map<String, String> configs = new HashMap<>();
        configs.put(RETENTION_MS_CONFIG, RETENTION_MS_30_MINUTES);
        return TopicBuilder.name(KAFKA_EMAIL_VERIFICATION_TOPIC)
                .partitions(3)
                .replicas(1)
                .configs(configs)
                .build();
    }

    @Bean
    public NewTopic paymentApprovedTopic() {
        return TopicBuilder.name(KAFKA_PAYMENT_APPROVED_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();

    }

    @Bean
    public NewTopic productTopic() {
        return TopicBuilder.name(KAFKA_PRODUCT_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();

    }
}
