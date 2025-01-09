package controller;

import TestClasses.VerificationCodeRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.DriverEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.DriverVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Status;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Type;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.AddressRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.DriverRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.LoginRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.LoginResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.PaginatedResponse;
import config.TestConfigs;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import testContainers.AbstractionIntegrationTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = RealTimeDeliveryTrackingSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class DriverAddressControllerTest extends AbstractionIntegrationTest {


    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static DriverVO driverVO;
    private static DriverEntity driverEntity;

    private static final String URL_PREFIX = "/driverAddress";

    private static final String VERIFICATION_CODE_URL_PREFIX = "/verificationCode";
    private static final String VERIFY_URL_PREFIX = "/verify";
    private static final String LOGIN_URL_PREFIX = "/api/login";
    private static final String HOST_PREFIX = "http://localhost:";


    private static final String ID = "d8e7df81-2cd4-41a2-a005-62e6d8079716";
    private static final String NAME = "Voyage";
    private static final String LICENSE_PLATE = "VQZ1F34";
    private static final Type TYPE = Type.CAR;
    private static final Status STATUS = Status.AVAILABLE;
    private static final String BEARER_PREFIX = "Bearer ";

    private static final String EMAIL = "driveraddress@gmail.com";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_DRIVER;


    private static final String PHONE_NUMBER = "5511995765432";
    private static final String DRIVER_LICENSE = "74635220400";
    private static final String STREET = "123 Main State";
    private static final String CITY = "Sample Cities";
    private static final String STATE = "Sample Statess";
    private static final String POSTAL_CODE = "12345111";
    private static final String COUNTRY = "Sample Countries";

    private static final boolean AUTHENTICATED = false;
    private static final LocalDateTime CODE_EXPIRATION = LocalDateTime.now().plusDays(5);
    private static final String VERIFY_CODE = "574077";


    @BeforeAll
    static void setUp(@Autowired PasswordEncoder passwordEncoder, @Autowired DriverRepository driverRepository, @Autowired UserRepository userRepository,
                      @Autowired AddressRepository addressRepository) {

        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        UserEntity userEntity = new UserEntity(ID, USERNAME, EMAIL, passwordEncoder.encode(PASSWORD), ROLE_NAME, VERIFY_CODE, AUTHENTICATED, CODE_EXPIRATION);
        AddressEntity addressEntity = new AddressEntity(ID, STREET, CITY, STATE, POSTAL_CODE, COUNTRY);
        addressRepository.save(addressEntity);
        userRepository.save(userEntity);

        addressEntity = new AddressEntity(ID, STREET, CITY, STATE, POSTAL_CODE, COUNTRY);
        addressRepository.save(addressEntity);
        VehicleEntity vehicleEntity = new VehicleEntity(ID, NAME, LICENSE_PLATE, TYPE, STATUS);
        driverVO = new DriverVO(ID, PHONE_NUMBER, DRIVER_LICENSE, new ArrayList<>(List.of(addressEntity))
                , userEntity, new ArrayList<>(List.of(vehicleEntity)));
        driverEntity = new DriverEntity(ID, PHONE_NUMBER, DRIVER_LICENSE, new ArrayList<>(List.of(addressEntity))
                , userEntity, new ArrayList<>(List.of(vehicleEntity)));


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

        LoginRequest loginRequest = new LoginRequest(driverEntity.getUser().getEmail(), PASSWORD);

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
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, BEARER_PREFIX + loginResponse.getToken())
                .setBaseUri(HOST_PREFIX + TestConfigs.SERVER_PORT)
                .setBasePath(URL_PREFIX)
                .disableCsrf()
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(3)
    void givenAddressObject_whenAddAddressToDriver_ShouldReturnAddressObject() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(driverVO.getAddresses().getFirst())
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        AddressVO createdAddress = objectMapper.readValue(content, AddressVO.class);

        Assertions.assertNotNull(createdAddress);
        Assertions.assertNotNull(createdAddress.getId());
        Assertions.assertTrue(createdAddress.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals(STREET, createdAddress.getStreet());
        assertEquals(CITY, createdAddress.getCity());
        assertEquals(STATE, createdAddress.getState());
        assertEquals(POSTAL_CODE, createdAddress.getPostalCode());
        assertEquals(COUNTRY, createdAddress.getCountry());

    }

    @Test
    @Order(4)
    void givenAddressObject_whenFindAllAddress_ShouldReturnAddressObjectList() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PaginatedResponse<AddressVO> paginatedResponse =
                objectMapper.readValue(content, new TypeReference<>() {
                });

        AddressVO paginatedAddress = paginatedResponse.getContent().getFirst();

        Assertions.assertNotNull(paginatedAddress);
        Assertions.assertNotNull(paginatedAddress.getId());
        Assertions.assertTrue(paginatedAddress.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        Assertions.assertNotNull(paginatedAddress);
        Assertions.assertNotNull(paginatedAddress.getId());
        Assertions.assertTrue(paginatedAddress.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals(STREET, paginatedAddress.getStreet());
        assertEquals(CITY, paginatedAddress.getCity());
        assertEquals(STATE, paginatedAddress.getState());
        assertEquals(POSTAL_CODE, paginatedAddress.getPostalCode());
        assertEquals(COUNTRY, paginatedAddress.getCountry());

    }
}



