package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.OrderEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.OrderStatus;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.OrderNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.OrderRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.OrderRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.OrderResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.OrderService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.customer.CustomerService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.TemporaryProductService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import constants.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {


    private static final String ORDER_NOT_FOUND_MESSAGE = "These order was not found!";

    private static final String EMAIL = "user@example.com";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_CUSTOMER;
    private static final String PHONE_NUMBER = "+5511998765432";
    private static final OrderStatus ORDER_STATUS = OrderStatus.PAYMENT_APPROVED;
    private static final BigDecimal TOTAL_PRICE = BigDecimal.valueOf(100.00);

    private OrderEntity orderEntity;
    private TemporaryProductVO temporaryProductVO;
    private OrderRequest orderRequest;
    private CustomerVO customerVO;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @Mock
    private OrderRepository repository;

    @Mock
    private CustomerService customerService;
    @Mock
    private TemporaryProductService temporaryProductService;

    @InjectMocks
    private OrderService orderService;




    @BeforeEach
    void setUp() {

        AddressEntity addressEntity = new AddressEntity(TestConstants.ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);

        UserEntity userEntity = new UserEntity(TestConstants.ID, TestConstants.USER_USERNAME,
                EMAIL, TestConstants.USER_PASSWORD, ROLE_NAME);

        CustomerEntity customerEntity = new CustomerEntity(TestConstants.ID, PHONE_NUMBER, new ArrayList<>(List.of(addressEntity)), userEntity);

        customerVO = new CustomerVO(TestConstants.ID, PHONE_NUMBER, new ArrayList<>(List.of(addressEntity)), userEntity);

        TemporaryProductEntity temporaryProductEntity = new TemporaryProductEntity(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE, TestConstants.PRODUCT_QUANTITY);

        temporaryProductVO = new TemporaryProductVO(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE, TestConstants.PRODUCT_QUANTITY);

        orderEntity = new OrderEntity(TestConstants.ID, customerEntity, List.of(temporaryProductEntity), TOTAL_PRICE, ORDER_STATUS);

        orderRequest = new OrderRequest(EMAIL, TOTAL_PRICE, List.of(temporaryProductVO), ORDER_STATUS);


        SecurityContextHolder.setContext(securityContext);


    }

    @Test
    void testCreateOrder_WhenSuccessful_ShouldReturnOrderResponseObject() {


        try (MockedStatic<ValidatorUtils> mockedValidatorUtils = mockStatic(ValidatorUtils.class)) {


            mockedValidatorUtils.when(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(),
                    anyString(), any())).thenAnswer(invocation -> null);
            when(customerService.findCustomerByEmail(EMAIL)).thenReturn(customerVO);
            when(repository.save(any(OrderEntity.class))).thenReturn(orderEntity);
            when(temporaryProductService.findTemporaryProductById(TestConstants.ID)).thenReturn(temporaryProductVO);

            OrderResponse order = orderService.createOrder(orderRequest);
            verify(customerService, times(1)).findCustomerByEmail(anyString());
            verify(temporaryProductService).findTemporaryProductById(anyString());
            verify(repository, times(1)).save(any(OrderEntity.class));

            mockedValidatorUtils.verify(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any()));


            assertNotNull(order);
            assertNotNull(order.getId());
            assertEquals(TOTAL_PRICE, order.getTotalPrice());
            assertEquals(ORDER_STATUS, order.getOrderStatus());
            assertEquals(1, order.getTemporaryProductEntities().size());
        }
    }

    @Test
    void testCreateOrder_WhenOrderIsNull_ShouldThrowOrderNotFoundException() {

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> orderService.createOrder(null));
        assertNotNull(exception);
        assertEquals(OrderNotFoundException.ERROR.formatErrorMessage(ORDER_NOT_FOUND_MESSAGE), exception.getMessage());
    }

    @Test
    void testFindOrderById_WhenOrderWasFound_ShouldReturnOrder() {

        when(repository.findById(TestConstants.ID)).thenReturn(Optional.of(orderEntity));
        OrderResponse orderById = orderService.findOrderById(TestConstants.ID);
        verify(repository, times(1)).findById(anyString());
        assertNotNull(orderById);
        assertEquals(TestConstants.ID, orderById.getId());
        assertEquals(TOTAL_PRICE, orderById.getTotalPrice());
        assertEquals(ORDER_STATUS, orderById.getOrderStatus());
        assertEquals(1, orderById.getTemporaryProductEntities().size());

    }

    @Test
    void testFindOrderById_WhenOrderWasNotFound_ShouldThrowOrderNotFoundException() {

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> orderService.findOrderById(TestConstants.ID));
        assertNotNull(exception);
        assertEquals(OrderNotFoundException.ERROR.formatErrorMessage(ORDER_NOT_FOUND_MESSAGE), exception.getMessage());
    }

    @Test
    void testFindAllOrders_WhenOrdersWasFound_ShouldReturnPaginatedOrders() {

        List<OrderEntity> orderEntities = List.of(orderEntity);
        Page<OrderEntity> orderPage = new PageImpl<>(orderEntities);
        Pageable pageable = Pageable.ofSize(10);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        when(repository.findAllByCustomerEmail(EMAIL, pageable)).thenReturn(orderPage);

        Page<OrderResponse> orders = orderService.findAllProducts(pageable);

        verify(repository, times(1))
                .findAllByCustomerEmail(anyString(), any(Pageable.class));

        OrderResponse orderResponse = orders.getContent().getFirst();

        assertNotNull(orderResponse);
        assertNotNull(orderResponse.getId());
        assertEquals(TOTAL_PRICE, orderResponse.getTotalPrice());
        assertEquals(ORDER_STATUS, orderResponse.getOrderStatus());
        assertEquals(1, orderResponse.getTemporaryProductEntities().size());

    }


}
