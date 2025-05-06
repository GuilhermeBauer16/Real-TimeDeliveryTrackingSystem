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
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.AddressRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.DriverRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.VehicleRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.LoginRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.LoginResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.mercadoPago.MercadoPagoService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.PaginatedResponse;
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
class VehicleControllerTest extends AbstractionIntegrationTest {

    @MockBean
    private MercadoPagoService mercadoPagoService;

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static VehicleVO vehicleVO;

    private static final String URL_PREFIX = "/vehicle";
    private static final String VERIFY_URL_PREFIX = "/verify";
    private static final String VERIFICATION_CODE_URL_PREFIX = "/verificationCode";
    private static final String LOGIN_URL_PREFIX = "/api/login";

    private static final String LICENSE_PLATE = "AQX1F34";
    private static final String SECOND_LICENSE_PLATE = "AAX1F34";

    private static final String UPDATED_LICENSE_PLATE = "AXA1F34";
    private static final String EMAIL = "johnzero@gmail.com";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_DRIVER;
    private static final String PHONE_NUMBER = "5511995765432";
    private static final String DRIVER_LICENSE = "82435476439";
    private static final boolean AUTHENTICATED = false;


    @BeforeAll
    static void setUp(@Autowired PasswordEncoder passwordEncoder, @Autowired DriverRepository driverRepository, @Autowired UserRepository userRepository,
                      @Autowired AddressRepository addressRepository, @Autowired VehicleRepository vehicleRepository) {
        objectMapper = new ObjectMapper();
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

        vehicleVO = new VehicleVO(TestConstants.ID, TestConstants.VEHICLE_NAME,
                SECOND_LICENSE_PLATE, TestConstants.VEHICLE_TYPE, TestConstants.VEHICLE_STATUS);

        vehicleRepository.save(vehicleEntity);

        DriverEntity driverEntity = new DriverEntity(TestConstants.ID, PHONE_NUMBER, DRIVER_LICENSE, new ArrayList<>(List.of(addressEntity))
                , new ArrayList<>(List.of(vehicleEntity)), userEntity);


        driverRepository.save(driverEntity);
    }

    @Test
    @Order(1)
    void givenVerificationCodeRequestObject_whenVerifyUser_ShouldReturnNothing() {

        VerificationCodeRequest verificationCodeRequestTest = new VerificationCodeRequest(EMAIL, TestConstants.USER_VERIFY_CODE,
                AUTHENTICATED, TestConstants.USER_CODE_EXPIRATION);


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

        LoginRequest loginRequest = new LoginRequest(EMAIL, TestConstants.USER_PASSWORD);

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


    @Test
    @Order(3)
    void givenVehicleObject_whenCreateVehicle_ShouldReturnVehicleObject() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(vehicleVO)
                .filter(new RequestLoggingFilter(LogDetail.ALL))
                .filter(new ResponseLoggingFilter(LogDetail.ALL))
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        VehicleVO createdVehicle = objectMapper.readValue(content, VehicleVO.class);

        Assertions.assertNotNull(createdVehicle);
        Assertions.assertNotNull(createdVehicle.getId());
        Assertions.assertTrue(createdVehicle.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertEquals(TestConstants.VEHICLE_NAME, createdVehicle.getName());
        assertEquals(SECOND_LICENSE_PLATE, createdVehicle.getLicensePlate());
        assertEquals(TestConstants.VEHICLE_TYPE, createdVehicle.getType());
        assertEquals(TestConstants.VEHICLE_STATUS, createdVehicle.getStatus());

        vehicleVO.setId(createdVehicle.getId());
    }

    @Test
    @Order(4)
    void givenVehicleObject_whenFindVehicleById_ShouldReturnVehicleObject() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", TestConstants.ID)
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        VehicleVO vehicle = objectMapper.readValue(content, VehicleVO.class);

        Assertions.assertNotNull(vehicle);
        Assertions.assertNotNull(vehicle.getId());
        Assertions.assertTrue(vehicle.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertEquals(TestConstants.VEHICLE_NAME, vehicle.getName());
        assertEquals(LICENSE_PLATE, vehicle.getLicensePlate());
        assertEquals(TestConstants.VEHICLE_TYPE, vehicle.getType());
        assertEquals(TestConstants.VEHICLE_STATUS, vehicle.getStatus());

    }

    @Test
    @Order(5)
    void givenVehicleObject_whenFindVehicleByLicensePlate_ShouldReturnVehicleObject() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("licensePlate", vehicleVO.getLicensePlate())
                .when()
                .get("/findByLicensePlate/{licensePlate}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        VehicleVO vehicle = objectMapper.readValue(content, VehicleVO.class);

        Assertions.assertNotNull(vehicle);
        Assertions.assertNotNull(vehicle.getId());
        Assertions.assertTrue(vehicle.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertEquals(TestConstants.VEHICLE_NAME, vehicle.getName());
        assertEquals(SECOND_LICENSE_PLATE, vehicle.getLicensePlate());
        assertEquals(TestConstants.VEHICLE_TYPE, vehicle.getType());
        assertEquals(TestConstants.VEHICLE_STATUS, vehicle.getStatus());

    }

    @Test
    @Order(6)
    void givenVehicleObject_whenFindAllVehicle_ShouldReturnVehicleObjectList() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PaginatedResponse<VehicleVO> paginatedResponse =
                objectMapper.readValue(content, new TypeReference<>() {
                });

        assertEquals(2, paginatedResponse.getContent().size());

    }

    @Test
    @Order(7)
    void givenVehicleObject_whenUpdateVehicle_ShouldReturnVehicleObject() throws JsonProcessingException {

        vehicleVO.setLicensePlate(UPDATED_LICENSE_PLATE);
        vehicleVO.setName(TestConstants.VEHICLE_UPDATED_NAME);

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(vehicleVO)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        VehicleVO vehicle = objectMapper.readValue(content, VehicleVO.class);

        Assertions.assertNotNull(vehicle);
        Assertions.assertNotNull(vehicle.getId());
        Assertions.assertTrue(vehicle.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertEquals(TestConstants.VEHICLE_UPDATED_NAME, vehicle.getName());
        assertEquals(UPDATED_LICENSE_PLATE, vehicle.getLicensePlate());
        assertEquals(TestConstants.VEHICLE_TYPE, vehicle.getType());
        assertEquals(TestConstants.VEHICLE_STATUS, vehicle.getStatus());

    }

    @Order(8)
    @Test
    void givenVehicleObject_when_delete_ShouldReturnNoContent() {

        given().spec(specification)
                .pathParam("id", vehicleVO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);

    }
}
