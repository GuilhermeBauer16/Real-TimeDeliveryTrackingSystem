package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.DriverVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.DriverRegistrationResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.mercadoPago.MercadoPagoService;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import config.TestConfigs;
import constants.TestConstants;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import testContainers.AbstractionIntegrationTest;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = RealTimeDeliveryTrackingSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class DriverRegistrationControllerTest extends AbstractionIntegrationTest {

//    @RegisterExtension
//    static GreenMailExtension greenMail =
//            new GreenMailExtension(ServerSetupTest.SMTP)
//                    .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke", "springboot"))
//                    .withPerMethodLifecycle(true);


    @MockBean
    private MercadoPagoService mercadoPagoService;

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static DriverVO driverVO;


    private static final String URL_PREFIX = "/signInDriver";

    private static final String LICENSE_PLATE = "AZX1F34";
    private static final String EMAIL = "john@gmail.com";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_DRIVER;
    private static final String PHONE_PREFIX = "+";
    private static final String PHONE_NUMBER = "5511995765432";
    private static final String DRIVER_LICENSE = "75526634674";


    @BeforeAll
    static void setUp(@Autowired PasswordEncoder passwordEncoder) {

        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        AddressEntity addressEntity = new AddressEntity(TestConstants.ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);

        UserEntity userEntity = new UserEntity(TestConstants.ID, TestConstants.USER_USERNAME,
                EMAIL,passwordEncoder.encode(TestConstants.USER_PASSWORD), ROLE_NAME);

        VehicleEntity vehicleEntity = new VehicleEntity(TestConstants.ID, TestConstants.VEHICLE_NAME,
                LICENSE_PLATE, TestConstants.VEHICLE_TYPE, TestConstants.VEHICLE_STATUS);

        driverVO = new DriverVO(TestConstants.ID, PHONE_NUMBER, DRIVER_LICENSE, new ArrayList<>(List.of(addressEntity))
                ,new ArrayList<>(List.of(vehicleEntity)), userEntity);

        specification = new RequestSpecBuilder()

                .setBaseUri(TestConstants.URL_HOST_PREFIX + TestConfigs.SERVER_PORT)
                .setBasePath(URL_PREFIX)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    void givenDriverObject_whenRegisterDriver_ShouldReturnDriverObject() throws JsonProcessingException {

        var content = given().spec(specification)
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(driverVO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        DriverRegistrationResponse createdDriver = objectMapper.readValue(content, DriverRegistrationResponse.class);

        Assertions.assertNotNull(createdDriver);
        Assertions.assertNotNull(createdDriver.getId());
        Assertions.assertTrue(createdDriver.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertNotNull(createdDriver);
        assertNotNull(createdDriver.getId());
        assertEquals(PHONE_PREFIX + PHONE_NUMBER, createdDriver.getPhoneNumber());
        assertEquals(1, createdDriver.getAddresses().size());
        assertEquals(EMAIL, createdDriver.getUserRegistrationResponse().getEmail());
        assertEquals(TestConstants.USER_USERNAME, createdDriver.getUserRegistrationResponse().getName());
        assertEquals(1, createdDriver.getVehicles().size());

        driverVO.setId(createdDriver.getId());
    }

}
