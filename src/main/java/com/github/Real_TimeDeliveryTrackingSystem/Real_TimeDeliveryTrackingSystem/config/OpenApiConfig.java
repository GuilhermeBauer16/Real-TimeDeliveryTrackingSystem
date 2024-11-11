package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Real_Time Delivery Tracking System API")
                        .version("v1")
                        .description("A Real_Time Delivery Tracking System API allows you to track your package " +
                                "and organize your orders.")
                        .license(
                                new License()
                                        .name("Apache 2.0")
                                        .url("https://springdoc.org/")
                        ));
    }
}
