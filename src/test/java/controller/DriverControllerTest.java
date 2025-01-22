package controller;

import TestClasses.VerificationCodeRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.dto.PasswordDTO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.DriverEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.DriverVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.AddressRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.DriverRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.LoginRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.LoginResponse;
import config.TestConfigs;
import constants.TestConstants;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import testContainers.AbstractionIntegrationTest;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = RealTimeDeliveryTrackingSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class DriverControllerTest extends AbstractionIntegrationTest {

    private static RequestSpecification specification;
    private static DriverVO driverVO;
    private static DriverEntity driverEntity;

    private static final String URL_PREFIX = "/driver";
    private static final String VERIFICATION_CODE_URL_PREFIX = "/verificationCode";
    private static final String VERIFY_URL_PREFIX = "/verify";
    private static final String LOGIN_URL_PREFIX = "/api/login";


    private static final String LICENSE_PLATE = "AQZ1F34";
    private static final String EMAIL = "driver@gmail.com";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_DRIVER;

    private static final String PHONE_NUMBER = "5511995765432";
    private static final String DRIVER_LICENSE = "82101864590";
    private static final boolean AUTHENTICATED = false;

    @BeforeAll
    static void setUp(@Autowired PasswordEncoder passwordEncoder, @Autowired DriverRepository driverRepository, @Autowired UserRepository userRepository,
                      @Autowired AddressRepository addressRepository) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        AddressEntity addressEntity = new AddressEntity(TestConstants.ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);

        UserEntity userEntity = new UserEntity(TestConstants.ID, TestConstants.USER_USERNAME,
                EMAIL,passwordEncoder.encode(TestConstants.USER_PASSWORD), ROLE_NAME,
                TestConstants.USER_VERIFY_CODE,AUTHENTICATED,TestConstants.USER_CODE_EXPIRATION);

        addressRepository.save(addressEntity);
        userRepository.save(userEntity);

        VehicleEntity vehicleEntity = new VehicleEntity(TestConstants.ID, TestConstants.VEHICLE_NAME,
                LICENSE_PLATE, TestConstants.VEHICLE_TYPE, TestConstants.VEHICLE_STATUS);

        driverVO = new DriverVO(TestConstants.ID, PHONE_NUMBER, DRIVER_LICENSE, new ArrayList<>(List.of(addressEntity))
                ,new ArrayList<>(List.of(vehicleEntity)), userEntity);

        driverEntity = new DriverEntity(TestConstants.ID, PHONE_NUMBER, DRIVER_LICENSE, new ArrayList<>(List.of(addressEntity))
                ,new ArrayList<>(List.of(vehicleEntity)), userEntity);


        driverRepository.save(driverEntity);
    }

    @Test
    @Order(1)
    void givenVerificationCodeRequestObject_whenVerifyUser_ShouldReturnNothing() {

        VerificationCodeRequest verificationCodeRequestTest = new VerificationCodeRequest(driverVO.getUser().getEmail(), driverVO.getUser().getVerifyCode(),
                driverVO.getUser().isAuthenticated(), driverVO.getUser().getCodeExpiration());


        given()
                .basePath(VERIFICATION_CODE_URL_PREFIX)
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(verificationCodeRequestTest)
                .filter(new RequestLoggingFilter(LogDetail.ALL))
                .filter(new ResponseLoggingFilter(LogDetail.ALL))
                .when()
                .post(VERIFY_URL_PREFIX)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

    }


    @Test
    @Order(2)
    void login() {

        LoginRequest loginRequest = new LoginRequest(driverEntity.getUser().getEmail(), TestConstants.USER_PASSWORD);

        LoginResponse loginResponse = given()
                .basePath(LOGIN_URL_PREFIX)
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(loginRequest)
                .filter(new RequestLoggingFilter(LogDetail.ALL))
                .filter(new ResponseLoggingFilter(LogDetail.ALL))
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body().as(LoginResponse.class);

        assertNotNull(loginResponse);


        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, TestConstants.URL_BEARER_PREFIX + loginResponse.getToken())
                .setBaseUri(TestConstants.URL_HOST_PREFIX + TestConfigs.SERVER_PORT)
                .setBasePath(URL_PREFIX)
                .disableCsrf()
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }


    @Order(3)
    @Test
    void givenDriverObject_when_delete_ShouldReturnNoContent() {
        PasswordDTO passwordDTO = new PasswordDTO(TestConstants.USER_PASSWORD);
        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(passwordDTO)
                .when()
                .delete()
                .then()
                .statusCode(204);

    }
}
