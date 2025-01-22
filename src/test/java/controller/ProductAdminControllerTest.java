package controller;

import TestClasses.VerificationCodeRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.LoginRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.LoginResponse;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import testContainers.AbstractionIntegrationTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = RealTimeDeliveryTrackingSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductAdminControllerTest extends AbstractionIntegrationTest {


    @RegisterExtension
    static GreenMailExtension greenMail =
            new GreenMailExtension(ServerSetupTest.SMTP)
                    .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke", "springboot"))
                    .withPerMethodLifecycle(true);

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;


    private static ProductVO productVO;

    private static final String URL_PREFIX = "/product";
    private static final String VERIFICATION_CODE_URL_PREFIX = "/verificationCode";
    private static final String VERIFY_URL_PREFIX = "/verify";
    private static final String LOGIN_URL_PREFIX = "/api/login";

    private static final String EMAIL = "adminProduct@example.com";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_ADMIN;
    private static final boolean AUTHENTICATED = false;


    @BeforeAll
    static void setUp(@Autowired PasswordEncoder passwordEncoder, @Autowired UserRepository userRepository) {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        productVO = new ProductVO(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE);

        UserEntity userEntity = new UserEntity(TestConstants.ID, TestConstants.USER_USERNAME,
                EMAIL,passwordEncoder.encode(TestConstants.USER_PASSWORD), ROLE_NAME,
                TestConstants.USER_VERIFY_CODE,AUTHENTICATED,TestConstants.USER_CODE_EXPIRATION);

        userRepository.save(userEntity);



    }

    @Test
    @Order(1)
    void givenCustomerObject_whenVerifyCustomer_ShouldReturnNothing() {

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
    void givenProductObject_whenCreateProduct_ShouldReturnProductObject() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(productVO)
                .filter(new RequestLoggingFilter(LogDetail.ALL))
                .filter(new ResponseLoggingFilter(LogDetail.ALL))
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        ProductVO product = objectMapper.readValue(content, ProductVO.class);

        Assertions.assertNotNull(product);
        Assertions.assertNotNull(product.getId());
        Assertions.assertTrue(product.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertEquals(TestConstants.PRODUCT_NAME, product.getName());
        assertEquals(TestConstants.PRODUCT_DESCRIPTION, product.getDescription());
        assertEquals(TestConstants.PRODUCT_PRICE, product.getPrice());

        productVO.setId(product.getId());
    }

    @Test
    @Order(4)
    void givenProductObject_whenUpdateProduct_ShouldReturnProductObject() throws JsonProcessingException {

        productVO.setPrice(TestConstants.PRODUCT_UPDATED_PRICE);
        productVO.setName(TestConstants.PRODUCT_UPDATED_NAME);

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(productVO)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        ProductVO product = objectMapper.readValue(content, ProductVO.class);

        Assertions.assertNotNull(product);
        Assertions.assertNotNull(product.getId());
        Assertions.assertTrue(product.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertEquals(TestConstants.PRODUCT_UPDATED_NAME, product.getName());
        assertEquals(TestConstants.PRODUCT_DESCRIPTION, product.getDescription());
        assertEquals(TestConstants.PRODUCT_UPDATED_PRICE, product.getPrice());

    }

    @Order(5)
    @Test
    void givenVehicleObject_when_delete_ShouldReturnNoContent() {

        given().spec(specification)
                .pathParam("id", productVO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);

    }


}
