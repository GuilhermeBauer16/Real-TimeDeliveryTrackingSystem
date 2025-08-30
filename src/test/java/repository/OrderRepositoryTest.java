package repository;


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
import constants.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import testContainers.AbstractionIntegrationTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = RealTimeDeliveryTrackingSystemApplication.class)
class OrderRepositoryTest extends AbstractionIntegrationTest {


    private static final String PHONE_NUMBER = "+5511998765432";
    private static final String EMAIL = "driveruser@example.com";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_CUSTOMER;
    private static final OrderStatus ORDER_STATUS = OrderStatus.PAYMENT_APPROVED;
    private static final BigDecimal TOTAL_PRICE = BigDecimal.valueOf(100.00);


    private OrderEntity orderEntity;

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final TemporaryProductRepository temporaryProductRepository;
    private final OrderRepository repository;


    @Autowired
    OrderRepositoryTest(UserRepository userRepository, AddressRepository addressRepository, CustomerRepository customerRepository, TemporaryProductRepository temporaryProductRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
        this.temporaryProductRepository = temporaryProductRepository;
        this.repository = orderRepository;
    }


    @BeforeEach
    void setUp() {

        AddressEntity addressEntity = new AddressEntity(TestConstants.ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);

        addressRepository.save(addressEntity);

        UserEntity userEntity = new UserEntity(TestConstants.ID, TestConstants.USER_USERNAME,
                EMAIL, TestConstants.USER_PASSWORD, ROLE_NAME);

        userRepository.save(userEntity);

        CustomerEntity customerEntity = new CustomerEntity(TestConstants.ID, PHONE_NUMBER, new ArrayList<>(List.of(addressEntity)), userEntity);

        customerRepository.save(customerEntity);


        TemporaryProductEntity temporaryProductEntity = new TemporaryProductEntity(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE, TestConstants.PRODUCT_QUANTITY);

        temporaryProductRepository.save(temporaryProductEntity);

        orderEntity = new OrderEntity(TestConstants.ID, customerEntity, List.of(temporaryProductEntity), TOTAL_PRICE, ORDER_STATUS);
        repository.save(orderEntity);


    }


    @Test
    void testFindAllByCustomerEmail_WhenCustomerWasFound_ShouldReturnOrdersPageableList() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderEntity> foundedOrderEntities = repository.findAllByCustomerEmail(orderEntity.getCustomer().getUser().getEmail(), pageable);
        OrderEntity foundedOrderEntity = foundedOrderEntities.getContent().getFirst();
        assertNotNull(foundedOrderEntities);
        assertNotNull(foundedOrderEntity);
        assertNotNull(foundedOrderEntity);
        assertEquals(TestConstants.ID, foundedOrderEntity.getId());
        assertEquals(TOTAL_PRICE, foundedOrderEntity.getTotalPrice());
        assertEquals(ORDER_STATUS, foundedOrderEntity.getOrderStatus());
        assertEquals(1, foundedOrderEntity.getTemporaryProductEntities().size());
        assertEquals(EMAIL, foundedOrderEntity.getCustomer().getUser().getEmail());


    }
}
