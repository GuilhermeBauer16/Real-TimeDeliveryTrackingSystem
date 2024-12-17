package repository;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.AddressRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.CustomerRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
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
    private static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
    private static final String STREET = "123 Main St";
    private static final String CITY = "Sample City";
    private static final String STATE = "Sample State";
    private static final String POSTAL_CODE = "12345";
    private static final String COUNTRY = "Sample Country";

    private static final String EMAIL = "customeruser@example.com";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
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

        AddressEntity addressEntity = new AddressEntity(ID, STREET, CITY, STATE, POSTAL_CODE, COUNTRY);
        addressRepository.save(addressEntity);
        UserEntity userEntity = new UserEntity(ID, USERNAME, EMAIL, PASSWORD, ROLE_NAME);
        userRepository.save(userEntity);
        customerEntity = new CustomerEntity(ID, PHONE_NUMBER, List.of(addressEntity), userEntity);
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
        assertEquals(ID, addressEntity.getId());
        assertEquals(STREET, addressEntity.getStreet());
        assertEquals(CITY, addressEntity.getCity());
        assertEquals(STATE, addressEntity.getState());
        assertEquals(POSTAL_CODE, addressEntity.getPostalCode());
        assertEquals(COUNTRY, addressEntity.getCountry());



    }

    @Test
    void testFindCustomerByUserEmail_WhenTheCustomerWasFound_ShouldReturnACustomerObject() {


        CustomerEntity foundedCustomer = repository.findCustomerByUserEmail(customerEntity.getUser().getEmail()).orElseThrow(null);

        assertNotNull(foundedCustomer);
        assertNotNull(foundedCustomer.getId());
        assertEquals(ID, foundedCustomer.getId());
        assertEquals(PHONE_NUMBER, foundedCustomer.getPhoneNumber());
        assertEquals(1, foundedCustomer.getAddresses().size());
        assertEquals(EMAIL, foundedCustomer.getUser().getEmail());
        assertEquals(USERNAME, foundedCustomer.getUser().getName());
        assertEquals(PASSWORD, foundedCustomer.getUser().getPassword());
        assertEquals(ROLE_NAME, foundedCustomer.getUser().getUserProfile());



    }
}
