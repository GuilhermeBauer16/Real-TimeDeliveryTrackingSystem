package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.EmailSenderService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.CodeGeneratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class RealTimeDeliveryTrackingSystemApplication implements CommandLineRunner {

    @Autowired
    EmailSenderService emailService;
    @Override
    public void run(String... args) throws Exception {

        emailService.sendValidatorCode("guilhermebauer16@gmail.com", CodeGeneratorUtils.generateCode(6));

    }

    public static void main(String[] args) {
        SpringApplication.run(RealTimeDeliveryTrackingSystemApplication.class, args);

    }
}


