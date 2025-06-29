package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.config;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.EmailVerificationMessageRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentApprovedMessageRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    private static final String EMAIL_VERIFICATION_GROUP_ID = "email-verification-group";

    private static final String PAYMENT_APPROVED_GROUP_ID = "payment-approved-group";
    private static final String KAFKA_OUTSET = "earliest";

    private final String bootstrapServer;

    public KafkaConsumerConfig(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        bootstrapServer = bootstrapServers;
    }

    @Bean
    public ConsumerFactory<String, EmailVerificationMessageRequest> emailVerificationConsumerFactory() {

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, EMAIL_VERIFICATION_GROUP_ID);
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, true);
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, EmailVerificationMessageRequest.class.getName());
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KAFKA_OUTSET);
        return new DefaultKafkaConsumerFactory<>(configProps);

    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmailVerificationMessageRequest> emailVerificationKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EmailVerificationMessageRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(emailVerificationConsumerFactory());
        return factory;
    }


    @Bean
    public ConsumerFactory<String, PaymentApprovedMessageRequest> paymentApprovedConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, PAYMENT_APPROVED_GROUP_ID);
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, true);
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, PaymentApprovedMessageRequest.class.getName());
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KAFKA_OUTSET);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentApprovedMessageRequest> paymentApprovedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentApprovedMessageRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentApprovedConsumerFactory());
        return factory;
    }
}
