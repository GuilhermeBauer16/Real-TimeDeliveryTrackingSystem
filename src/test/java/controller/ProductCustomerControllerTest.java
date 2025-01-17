package controller;

import TestClasses.VerificationCodeRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.AddressRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.CustomerRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.ProductRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.LoginRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.LoginResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.PaginatedResponse;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
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
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import testContainers.AbstractionIntegrationTest;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = RealTimeDeliveryTrackingSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductCustomerControllerTest extends AbstractionIntegrationTest {


    @RegisterExtension
    static GreenMailExtension greenMail =
            new GreenMailExtension(ServerSetupTest.SMTP)
                    .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke", "springboot"))
                    .withPerMethodLifecycle(true);

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static CustomerVO customerVO;
    private static CustomerEntity customerEntity;
    private static ProductVO productVO;

    private static final String URL_PREFIX = "/customer/product";
    private static final String VERIFICATION_CODE_URL_PREFIX = "/verificationCode";
    private static final String VERIFY_URL_PREFIX = "/verify";
    private static final String LOGIN_URL_PREFIX = "/api/login";
    private static final String HOST_PREFIX = "http://localhost:";
    private static final String BEARER_PREFIX = "Bearer ";


    private static final String PHONE_NUMBER = "5511998765432";
    private static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
    private static final String STREET = "123 Main St";
    private static final String CITY = "Sample City";
    private static final String STATE = "Sample State";
    private static final String POSTAL_CODE = "12345";
    private static final String COUNTRY = "Sample Country";
    private static final String EMAIL = "customerProduct@example.com";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_CUSTOMER;
    private static final boolean AUTHENTICATED = false;
    private static final LocalDateTime CODE_EXPIRATION = LocalDateTime.now().plusDays(5);
    private static final String VERIFY_CODE = "574077";

    private static final String PRODUCT_NAME = "Shoes";
    private static final String PRODUCT_DESCRIPTION = "That is the new version of the Shoes";
    private static final Double PRODUCT_PRICE = 100D;

    @BeforeAll
    static void setUp(@Autowired PasswordEncoder passwordEncoder, @Autowired CustomerRepository customerRepository, @Autowired UserRepository userRepository,
                      @Autowired AddressRepository addressRepository, @Autowired
                      ProductRepository productRepository) {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        productVO = new ProductVO(ID,PRODUCT_NAME,PRODUCT_DESCRIPTION,PRODUCT_PRICE);

        UserEntity userEntity = new UserEntity(ID, USERNAME, EMAIL, passwordEncoder.encode(PASSWORD), ROLE_NAME, VERIFY_CODE, AUTHENTICATED, CODE_EXPIRATION);
        AddressEntity addressEntity = new AddressEntity(ID, STREET, CITY, STATE, POSTAL_CODE, COUNTRY);
        addressRepository.save(addressEntity);
        userRepository.save(userEntity);
        customerVO = new CustomerVO(ID, PHONE_NUMBER, List.of(addressEntity), userEntity);
        customerEntity = new CustomerEntity(ID, PHONE_NUMBER, List.of(addressEntity), userEntity);
        customerRepository.save(customerEntity);

        ProductEntity productEntity = new ProductEntity(ID, PRODUCT_NAME, PRODUCT_DESCRIPTION, PRODUCT_PRICE);
        productRepository.save(productEntity);


    }

    @Test
    @Order(1)
    void givenCustomerObject_whenVerifyCustomer_ShouldReturnNothing() {

        VerificationCodeRequest verificationCodeRequestTest = new VerificationCodeRequest(customerVO.getUser().getEmail(), customerVO.getUser().getVerifyCode(),
                customerVO.getUser().isAuthenticated(), customerVO.getUser().getCodeExpiration());


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

        LoginRequest loginRequest = new LoginRequest(customerEntity.getUser().getEmail(), PASSWORD);

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
    @Order(4)
    void givenProductObject_whenFindProductById_ShouldReturnProductObject() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", productVO.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        ProductVO product = objectMapper.readValue(content, ProductVO.class);

        Assertions.assertNotNull(product);
        Assertions.assertNotNull(product.getId());
        Assertions.assertTrue(product.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertEquals(PRODUCT_NAME, product.getName());
        assertEquals(PRODUCT_DESCRIPTION, product.getDescription());
        assertEquals(PRODUCT_PRICE, product.getPrice());

    }

    @Test
    @Order(5)
    void givenProductObject_whenFindAllProductsByName_ShouldReturnProductObjectList() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get("/findByName/{name}",productVO.getName())
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PaginatedResponse<ProductVO> paginatedResponse =
                objectMapper.readValue(content, new TypeReference<>() {
                });

        assertEquals(1, paginatedResponse.getContent().size());

    }

    @Test
    @Order(6)
    void givenProductObject_whenFindAllProducts_ShouldReturnProductObjectList(){

        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();


    }


}
