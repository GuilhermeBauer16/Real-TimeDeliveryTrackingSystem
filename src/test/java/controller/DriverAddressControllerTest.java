package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.DriverVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Status;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Type;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.AddressRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.LoginRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.DriverRegistrationResponse;
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
    private static AddressEntity addressEntity;

    private static final String URL_PREFIX = "/driverAddress";
    private static final String SIGN_IN_URL_PREFIX = "/signInDriver";
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

    private static final String PHONE_PREFIX = "+";
    private static final String PHONE_NUMBER = "5511995765432";
    private static final String DRIVER_LICENSE = "74635220400";
    private static final String STREET = "123 Main State";
    private static final String CITY = "Sample Cities";
    private static final String STATE = "Sample Statess";
    private static final String POSTAL_CODE = "12345111";
    private static final String COUNTRY = "Sample Countries";


    @BeforeAll
    static void setUp(@Autowired AddressRepository addressRepository, @Autowired PasswordEncoder passwordEncoder) {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        UserEntity userEntity = new UserEntity(ID, USERNAME, EMAIL, passwordEncoder.encode(PASSWORD), ROLE_NAME);
        addressEntity = new AddressEntity(ID, STREET, CITY, STATE, POSTAL_CODE, COUNTRY);
        addressRepository.save(addressEntity);
        VehicleEntity vehicleEntity = new VehicleEntity(ID, NAME, LICENSE_PLATE, TYPE, STATUS);
        driverVO = new DriverVO(ID, PHONE_NUMBER, DRIVER_LICENSE, new ArrayList<>(List.of(addressEntity))
                , userEntity, new ArrayList<>(List.of(vehicleEntity)));
    }

    @Test
    @Order(1)
    void givenDriverObject_whenRegisterDriver_ShouldReturnDriverObject() throws JsonProcessingException {

        var content = given()
                .basePath(SIGN_IN_URL_PREFIX)
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
        assertEquals(USERNAME, createdDriver.getUserRegistrationResponse().getName());

        driverVO.setId(createdDriver.getId());
    }

    @Test
    @Order(2)
    void login() {
        LoginRequest loginRequest = new LoginRequest(driverVO.getUser().getEmail(), driverVO.getUser().getPassword());

        var accessToken = given()
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
                .body().as(LoginResponse.class).getToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, BEARER_PREFIX + accessToken)
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
        driverVO.getAddresses().add(addressEntity);
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(driverVO.getAddresses().get(1))
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
        driverVO.getAddresses().add(addressEntity);
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



