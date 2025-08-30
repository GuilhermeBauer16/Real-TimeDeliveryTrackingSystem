package controller;

import TestClasses.VerificationCodeRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.OrderEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.OrderStatus;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.AddressRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.CustomerRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.OrderRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.TemporaryProductRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.LoginRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.LoginResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.OrderResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.mercadoPago.MercadoPagoService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.PaginatedResponse;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import testContainers.AbstractionIntegrationTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = RealTimeDeliveryTrackingSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class OrderControllerTest extends AbstractionIntegrationTest {

    @MockBean
    private MercadoPagoService mercadoPagoService;


    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;


    private static final String URL_PREFIX = "/order";
    private static final String VERIFICATION_CODE_URL_PREFIX = "/verificationCode";
    private static final String VERIFY_URL_PREFIX = "/verify";
    private static final String LOGIN_URL_PREFIX = "/api/login";

    private static final String PHONE_NUMBER = "5511998765432";
    private static final String EMAIL = "customerShoppingCart@example.com";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_CUSTOMER;
    private static final OrderStatus ORDER_STATUS = OrderStatus.PAYMENT_APPROVED;
    private static final BigDecimal TOTAL_PRICE = BigDecimal.valueOf(100.00);
    private static final boolean AUTHENTICATED = false;


    @BeforeAll
    static void setUp(@Autowired PasswordEncoder passwordEncoder, @Autowired CustomerRepository customerRepository, @Autowired UserRepository userRepository,
                      @Autowired AddressRepository addressRepository
            , @Autowired TemporaryProductRepository temporaryProductRepository,
                      @Autowired OrderRepository orderRepository) {

        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


        AddressEntity addressEntity = new AddressEntity(TestConstants.ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);

        UserEntity userEntity = new UserEntity(TestConstants.ID, TestConstants.USER_USERNAME,
                EMAIL, passwordEncoder.encode(TestConstants.USER_PASSWORD), ROLE_NAME,
                TestConstants.USER_VERIFY_CODE, AUTHENTICATED, TestConstants.USER_CODE_EXPIRATION);

        addressRepository.save(addressEntity);
        userRepository.save(userEntity);

        TemporaryProductEntity temporaryProductEntity = new TemporaryProductEntity(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE, TestConstants.PRODUCT_QUANTITY);

        temporaryProductRepository.save(temporaryProductEntity);


        CustomerEntity customerEntity = new CustomerEntity(TestConstants.ID, PHONE_NUMBER, new ArrayList<>(List.of(addressEntity)), userEntity);

        customerRepository.save(customerEntity);


        OrderEntity orderEntity = new OrderEntity(TestConstants.ID, customerEntity, List.of(temporaryProductEntity), TOTAL_PRICE, ORDER_STATUS);
        orderRepository.save(orderEntity);


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
    void givenOrderById_WhenOrderIsFound_ShouldReturnOrderResponseObject() throws JsonProcessingException {

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


        OrderResponse orderResponse = objectMapper.readValue(content, OrderResponse.class);
        assertNotNull(orderResponse);
        assertNotNull(orderResponse.getId());
        assertEquals(ORDER_STATUS, orderResponse.getOrderStatus());
        assertEquals(1, orderResponse.getTemporaryProductEntities().size());


    }

    @Test
    @Order(4)
    void givenAllOrders_WhenFindAllOrders_ShouldReturnAllOrders() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .filter(new RequestLoggingFilter(LogDetail.ALL))
                .filter(new ResponseLoggingFilter(LogDetail.ALL))
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PaginatedResponse<OrderResponse> paginatedResponse =
                objectMapper.readValue(content, new TypeReference<>() {
                });

        assertEquals(1, paginatedResponse.getContent().size());
    }


}
