package controller;

import TestClasses.VerificationCodeRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.AddressRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.CustomerRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.LoginRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.LoginResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.PaginatedResponse;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import testContainers.AbstractionIntegrationTest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = RealTimeDeliveryTrackingSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CustomerAddressControllerTest extends AbstractionIntegrationTest {

    @RegisterExtension
    static GreenMailExtension greenMail =
            new GreenMailExtension(ServerSetupTest.SMTP)
                    .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke", "springboot"))
                    .withPerMethodLifecycle(true);

    @Autowired
    private JavaMailSender javaMailSender;

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static CustomerVO customerVO;
    private static AddressVO addressVO;


    private static final String URL_PREFIX = "/customerAddress";
    private static final String VERIFICATION_CODE_URL_PREFIX = "/verificationCode";
    private static final String VERIFY_URL_PREFIX = "/verify";
    private static final String LOGIN_URL_PREFIX = "/api/login";
    
    private static final String PHONE_NUMBER = "5511998765432";
    private static final String EMAIL = "customerAddress@example.com";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_CUSTOMER;
    private static final boolean AUTHENTICATED = false;


    @BeforeAll
    static void setUp(@Autowired PasswordEncoder passwordEncoder, @Autowired CustomerRepository customerRepository, @Autowired UserRepository userRepository,
                      @Autowired AddressRepository addressRepository) {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        AddressEntity addressEntity = new AddressEntity(TestConstants.ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);

        UserEntity userEntity = new UserEntity(TestConstants.ID, TestConstants.USER_USERNAME,
                EMAIL, passwordEncoder.encode(TestConstants.USER_PASSWORD), ROLE_NAME, TestConstants.USER_VERIFY_CODE,AUTHENTICATED,TestConstants.USER_CODE_EXPIRATION);

        addressRepository.save(addressEntity);
        userRepository.save(userEntity);
        addressVO = new AddressVO(TestConstants.ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);

        customerVO = new CustomerVO(TestConstants.ID, PHONE_NUMBER, List.of(addressEntity), userEntity);
        CustomerEntity customerEntity = new CustomerEntity(TestConstants.ID, PHONE_NUMBER, List.of(addressEntity), userEntity);
        customerRepository.save(customerEntity);


    }


    @Test
    @Order(1)
    void givenCustomerObject_whenVerifyCustomer_ShouldReturnNothing() {

        VerificationCodeRequest verificationCodeRequestTest = new VerificationCodeRequest(customerVO.getUser().getEmail(),
                customerVO.getUser().getVerifyCode(), customerVO.getUser().isAuthenticated(), customerVO.getUser().getCodeExpiration());


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
    void givenAddressObject_whenAddAddressToCustomer_ShouldReturnAddressObject() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(addressVO)
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
        assertTrue(createdAddress.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertEquals(TestConstants.ADDRESS_STREET, createdAddress.getStreet());
        assertEquals(TestConstants.ADDRESS_CITY, createdAddress.getCity());
        assertEquals(TestConstants.ADDRESS_STATE, createdAddress.getState());
        assertEquals(TestConstants.ADDRESS_POSTAL_CODE, createdAddress.getPostalCode());
        assertEquals(TestConstants.ADDRESS_COUNTRY, createdAddress.getCountry());

    }

    @Test
    @Order(4)
    void givenAddressObject_whenFindAddressToCustomer_ShouldReturnAddressObject() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .filter(new RequestLoggingFilter(LogDetail.ALL))
                .filter(new ResponseLoggingFilter(LogDetail.ALL))
                .get("/{id}", TestConstants.ID)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        AddressVO address = objectMapper.readValue(content, AddressVO.class);

        Assertions.assertNotNull(address);
        Assertions.assertNotNull(address.getId());
        assertTrue(address.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertEquals(TestConstants.ADDRESS_STREET, address.getStreet());
        assertEquals(TestConstants.ADDRESS_CITY, address.getCity());
        assertEquals(TestConstants.ADDRESS_STATE, address.getState());
        assertEquals(TestConstants.ADDRESS_POSTAL_CODE, address.getPostalCode());
        assertEquals(TestConstants.ADDRESS_COUNTRY, address.getCountry());


    }


    @Test
    @Order(5)
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
        assertTrue(paginatedAddress.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        Assertions.assertNotNull(paginatedAddress);
        Assertions.assertNotNull(paginatedAddress.getId());
        assertTrue(paginatedAddress.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
        assertEquals(TestConstants.ADDRESS_STREET, paginatedAddress.getStreet());
        assertEquals(TestConstants.ADDRESS_CITY, paginatedAddress.getCity());
        assertEquals(TestConstants.ADDRESS_STATE, paginatedAddress.getState());
        assertEquals(TestConstants.ADDRESS_POSTAL_CODE, paginatedAddress.getPostalCode());
        assertEquals(TestConstants.ADDRESS_COUNTRY, paginatedAddress.getCountry());


    }

    @Test
    @Order(6)
    void givenAddressObject_whenUpdateAddressToCustomer_ShouldReturnAddressObject() throws JsonProcessingException {


        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(addressVO)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        AddressVO address = objectMapper.readValue(content, AddressVO.class);

        Assertions.assertNotNull(address);
        Assertions.assertNotNull(address.getId());
        assertTrue(address.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertEquals(TestConstants.ADDRESS_STREET, address.getStreet());
        assertEquals(TestConstants.ADDRESS_CITY, address.getCity());
        assertEquals(TestConstants.ADDRESS_STATE, address.getState());
        assertEquals(TestConstants.ADDRESS_POSTAL_CODE, address.getPostalCode());
        assertEquals(TestConstants.ADDRESS_COUNTRY, address.getCountry());


    }

    @Test
    @Order(7)
    void givenAddressObject_whenDeleteAddress_ShouldDoNothing() {

        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .filter(new RequestLoggingFilter(LogDetail.ALL))
                .filter(new ResponseLoggingFilter(LogDetail.ALL))
                .delete("/{id}", TestConstants.ID)
                .then()
                .statusCode(204)
                .extract()
                .body()
                .asString();
    }


}
