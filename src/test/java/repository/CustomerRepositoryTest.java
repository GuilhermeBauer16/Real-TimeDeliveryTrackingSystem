package repository;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.AddressRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.CustomerRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = RealTimeDeliveryTrackingSystemApplication.class)
class CustomerRepositoryTest extends AbstractionIntegrationTest {


    private static final String PHONE_NUMBER = "+5511998765432";
    private static final String EMAIL = "customeruser@example.com";

    private static final UserProfile ROLE_NAME = UserProfile.ROLE_CUSTOMER;


    private CustomerEntity customerEntity;



    private final CustomerRepository repository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Autowired
    CustomerRepositoryTest(CustomerRepository repository, AddressRepository addressRepository, UserRepository userRepository) {
        this.repository = repository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }


    @BeforeEach
    void setUp() {

        AddressEntity addressEntity = new AddressEntity(TestConstants.ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);

        UserEntity userEntity = new UserEntity(TestConstants.ID, TestConstants.USER_USERNAME,
                EMAIL,TestConstants.USER_PASSWORD, ROLE_NAME);

        addressRepository.save(addressEntity);
        userRepository.save(userEntity);

        customerEntity = new CustomerEntity(TestConstants.ID, PHONE_NUMBER, List.of(addressEntity), userEntity);
        repository.save(customerEntity);

    }



    @Test
    void testFindAddressesByCustomerEmail_WhenTheAddressesWasFound_ShouldReturnAAddressPageableList() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<AddressEntity> foundedAddresses = repository.findAddressesByCustomerEmail(customerEntity.getUser().getEmail(), pageable);
        AddressEntity addressEntity = foundedAddresses.getContent().getFirst();

        assertNotNull(foundedAddresses);
        assertNotNull(addressEntity);
        assertNotNull(addressEntity.getId());
        assertEquals(TestConstants.ID, addressEntity.getId());
        assertEquals(TestConstants.ADDRESS_STREET, addressEntity.getStreet());
        assertEquals(TestConstants.ADDRESS_CITY, addressEntity.getCity());
        assertEquals(TestConstants.ADDRESS_STATE, addressEntity.getState());
        assertEquals(TestConstants.ADDRESS_POSTAL_CODE, addressEntity.getPostalCode());
        assertEquals(TestConstants.ADDRESS_COUNTRY, addressEntity.getCountry());



    }

    @Test
    void testFindCustomerByUserEmail_WhenTheCustomerWasFound_ShouldReturnACustomerObject() {


        CustomerEntity foundedCustomer = repository.findCustomerByUserEmail(customerEntity.getUser().getEmail()).orElseThrow(null);

        assertNotNull(foundedCustomer);
        assertNotNull(foundedCustomer.getId());
        assertEquals(TestConstants.ID, foundedCustomer.getId());
        assertEquals(PHONE_NUMBER, foundedCustomer.getPhoneNumber());
        assertEquals(1, foundedCustomer.getAddresses().size());
        assertEquals(EMAIL, foundedCustomer.getUser().getEmail());
        assertEquals(TestConstants.USER_USERNAME, foundedCustomer.getUser().getName());
        assertEquals(TestConstants.USER_PASSWORD, foundedCustomer.getUser().getPassword());
        assertEquals(ROLE_NAME, foundedCustomer.getUser().getUserProfile());



    }
}
