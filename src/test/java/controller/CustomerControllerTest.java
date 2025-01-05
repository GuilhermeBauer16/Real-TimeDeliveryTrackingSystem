package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.dto.PasswordDTO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.LoginRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.CustomerRegistrationResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.LoginResponse;
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

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = RealTimeDeliveryTrackingSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CustomerControllerTest extends AbstractionIntegrationTest {


    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static CustomerVO customerVO;

    private static final String URL_PREFIX = "/customer";
    private static final String SIGN_IN_URL_PREFIX = "/signInCustomer";
    private static final String LOGIN_URL_PREFIX = "/api/login";
    private static final String HOST_PREFIX = "http://localhost:";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String PHONE_PREFIX = "+";

    private static final String PHONE_NUMBER = "5511998765432";
    private static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
    private static final String STREET = "123 Main St";
    private static final String CITY = "Sample City";
    private static final String STATE = "Sample State";
    private static final String POSTAL_CODE = "12345";
    private static final String COUNTRY = "Sample Country";
    private static final String EMAIL = "customer@example.com";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_CUSTOMER;

    @BeforeAll
    static void setUp(@Autowired PasswordEncoder passwordEncoder) {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        UserEntity userEntity = new UserEntity(ID, USERNAME, EMAIL, passwordEncoder.encode(PASSWORD), ROLE_NAME);
        AddressEntity addressEntity = new AddressEntity(ID, STREET, CITY, STATE, POSTAL_CODE, COUNTRY);

        customerVO = new CustomerVO(ID, PHONE_NUMBER, List.of(addressEntity), userEntity);


    }

    @Test
    @Order(1)
    void givenCustomerObject_whenCreateCustomer_ShouldReturnCustomerObject() throws JsonProcessingException {

        var content = given()
                .basePath(SIGN_IN_URL_PREFIX)
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(customerVO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        CustomerRegistrationResponse createdCustomer = objectMapper.readValue(content, CustomerRegistrationResponse.class);

        Assertions.assertNotNull(createdCustomer);
        Assertions.assertNotNull(createdCustomer.getId());
        Assertions.assertTrue(createdCustomer.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertNotNull(createdCustomer);
        assertNotNull(createdCustomer.getId());
        assertEquals(PHONE_PREFIX + PHONE_NUMBER, createdCustomer.getPhoneNumber());
        assertEquals(1, createdCustomer.getAddresses().size());
        assertEquals(EMAIL, createdCustomer.getUserRegistrationResponse().getEmail());
        assertEquals(USERNAME, createdCustomer.getUserRegistrationResponse().getName());

        customerVO.setId(createdCustomer.getId());
    }

    @Test
    @Order(2)
    void login() {
        LoginRequest loginRequest = new LoginRequest(customerVO.getUser().getEmail(), customerVO.getUser().getPassword());

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


    @Order(3)
    @Test
    void givenCustomerObject_when_delete_ShouldReturnNoContent() {
        PasswordDTO passwordDTO = new PasswordDTO(customerVO.getUser().getPassword());
        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(passwordDTO)
                .when()
                .delete()
                .then()
                .statusCode(204);

    }



}
