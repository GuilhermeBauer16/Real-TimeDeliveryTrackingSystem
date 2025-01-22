//package controller;
//
//import TestClasses.VerificationCodeRequest;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.dto.PasswordDTO;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.AddressRepository;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.CustomerRepository;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.LoginRequest;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.LoginResponse;
//import config.TestConfigs;
//import constants.TestConstants;
//import io.restassured.builder.RequestSpecBuilder;
//import io.restassured.filter.log.LogDetail;
//import io.restassured.filter.log.RequestLoggingFilter;
//import io.restassured.filter.log.ResponseLoggingFilter;
//import io.restassured.specification.RequestSpecification;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.annotation.DirtiesContext;
//import testContainers.AbstractionIntegrationTest;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static io.restassured.RestAssured.given;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@SpringBootTest(classes = RealTimeDeliveryTrackingSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//class CustomerControllerTest extends AbstractionIntegrationTest {
//
//
//    private static RequestSpecification specification;
//
//    private static final String URL_PREFIX = "/customer";
//    private static final String VERIFICATION_CODE_URL_PREFIX = "/verificationCode";
//    private static final String VERIFY_URL_PREFIX = "/verify";
//    private static final String LOGIN_URL_PREFIX = "/api/login";
//
//    private static final String PHONE_NUMBER = "5511998765432";
//    private static final String EMAIL = "customerController@example.com";
//    private static final UserProfile ROLE_NAME = UserProfile.ROLE_CUSTOMER;
//    private static final boolean AUTHENTICATED = false;
//    public static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
//
//
//    @BeforeAll
//    static void setUp(@Autowired PasswordEncoder passwordEncoder, @Autowired CustomerRepository customerRepository, @Autowired UserRepository userRepository,
//                      @Autowired AddressRepository addressRepository) {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//
//        AddressEntity addressEntity = new AddressEntity(ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
//                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);
//
//
//        UserEntity userEntity = new UserEntity(ID, TestConstants.USER_USERNAME,
//                EMAIL, passwordEncoder.encode(TestConstants.USER_PASSWORD), ROLE_NAME,
//                TestConstants.USER_VERIFY_CODE, AUTHENTICATED, TestConstants.USER_CODE_EXPIRATION);
//
//        addressRepository.save(addressEntity);
//        userRepository.save(userEntity);
//
//        CustomerEntity customerEntity = new CustomerEntity(ID, PHONE_NUMBER, new ArrayList<>(List.of(addressEntity)), userEntity);
//
//        customerRepository.save(customerEntity);
//
//
//    }
//
//    @Test
//    @Order(1)
//    void givenCustomerObject_whenVerifyCustomer_ShouldReturnNothing() {
//
//        VerificationCodeRequest verificationCodeRequestTest = new VerificationCodeRequest(EMAIL, TestConstants.USER_VERIFY_CODE,
//                AUTHENTICATED, TestConstants.USER_CODE_EXPIRATION);
//
//
//        given()
//                .basePath(VERIFICATION_CODE_URL_PREFIX)
//                .port(TestConfigs.SERVER_PORT)
//                .contentType(TestConfigs.CONTENT_TYPE_JSON)
//                .body(verificationCodeRequestTest)
//                .filter(new RequestLoggingFilter(LogDetail.ALL))
//                .filter(new ResponseLoggingFilter(LogDetail.ALL))
//                .when()
//                .post(VERIFY_URL_PREFIX)
//                .then()
//                .statusCode(200)
//                .extract()
//                .body()
//                .asString();
//
//    }
//
//
//    @Test
//    @Order(2)
//    void login() {
//
//        LoginRequest loginRequest = new LoginRequest(EMAIL, TestConstants.USER_PASSWORD);
//
//        LoginResponse loginResponse = given()
//                .basePath(LOGIN_URL_PREFIX)
//                .port(TestConfigs.SERVER_PORT)
//                .contentType(TestConfigs.CONTENT_TYPE_JSON)
//                .body(loginRequest)
//                .filter(new RequestLoggingFilter(LogDetail.ALL))
//                .filter(new ResponseLoggingFilter(LogDetail.ALL))
//                .when()
//                .post()
//                .then()
//                .statusCode(200)
//                .extract()
//                .body().as(LoginResponse.class);
//
//        assertNotNull(loginResponse);
//
//
//        specification = new RequestSpecBuilder()
//                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, TestConstants.URL_BEARER_PREFIX + loginResponse.getToken())
//                .setBaseUri(TestConstants.URL_HOST_PREFIX + TestConfigs.SERVER_PORT)
//                .setBasePath(URL_PREFIX)
//                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
//                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
//                .build();
//    }
//
//    @Order(3)
//    @Test
//    void givenCustomerObject_when_delete_ShouldReturnNoContent() {
//
//        PasswordDTO passwordDTO = new PasswordDTO(TestConstants.USER_PASSWORD);
//        given().spec(specification)
//                .contentType(TestConfigs.CONTENT_TYPE_JSON)
//                .body(passwordDTO)
//                .when()
//                .delete()
//                .then()
//                .statusCode(204);
//
//    }
//
//
//}
