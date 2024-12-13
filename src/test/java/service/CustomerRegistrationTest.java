package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidCustomerException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.CustomerRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.CustomerRegistrationResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.AddressService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.CustomerRegistrationService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.UserRegistrationService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.PhoneNumberValidator;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

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
class CustomerRegistrationTest {

    private static final String INVALID_CUSTOMER_MESSAGE = "This customer is invalid, please verify the fields and try again.";


    private static final String PHONE_NUMBER = "+5511998765432";
    private static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
    private static final String STREET = "123 Main St";
    private static final String CITY = "Sample City";
    private static final String STATE = "Sample State";
    private static final String POSTAL_CODE = "12345";
    private static final String COUNTRY = "Sample Country";

    private static final String EMAIL = "user@example.com";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_CUSTOMER;

    private AddressVO addressVO;
    private UserVO userVO;
    private CustomerEntity customerEntity;
    private CustomerVO customerVO;

    @Mock
    private CustomerRepository repository;

    @Mock
    private AddressService addressService;

    @Mock
    private UserRegistrationService userRegistrationService;

    @InjectMocks
    private CustomerRegistrationService service;

    @BeforeEach
    public void setUp() {

        AddressEntity addressEntity = new AddressEntity(ID, STREET, CITY, STATE, POSTAL_CODE, COUNTRY);
        addressVO = new AddressVO(ID, STREET, CITY, STATE, POSTAL_CODE, COUNTRY);
        UserEntity userEntity = new UserEntity(ID, USERNAME, EMAIL, PASSWORD, ROLE_NAME);
        userVO = new UserVO(ID, USERNAME, EMAIL, PASSWORD, ROLE_NAME);
        customerEntity = new CustomerEntity(ID, PHONE_NUMBER, Set.of(addressEntity), userEntity);
        customerVO = new CustomerVO(ID, PHONE_NUMBER, Set.of(addressEntity), userEntity);

    }

    @Test
    void testCreateCustomer_WhenSuccessful_ShouldReturnCustomerObject() {

        try (MockedStatic<ValidatorUtils> mockedValidatorUtils = mockStatic(ValidatorUtils.class);
             MockedStatic<PhoneNumberValidator> mockedPhoneNumberValidator = mockStatic(PhoneNumberValidator.class)) {

            mockedValidatorUtils.when(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any()))
                    .thenAnswer(invocation -> null);
            mockedValidatorUtils.when(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any()))
                    .thenAnswer(invocation -> null);
            mockedPhoneNumberValidator.when(() -> PhoneNumberValidator.validatePhoneNumber(anyString()))
                    .thenAnswer(invocation -> null);

            when(repository.save(any(CustomerEntity.class))).thenReturn(customerEntity);
            when(userRegistrationService.createUser(any(UserVO.class))).thenReturn(userVO);
            when(addressService.create(any(AddressVO.class))).thenReturn(addressVO);


            CustomerRegistrationResponse customer = service.create(customerVO);

            mockedValidatorUtils.verify(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any()));
            mockedValidatorUtils.verify(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any()));
            mockedPhoneNumberValidator.verify(() -> PhoneNumberValidator.validatePhoneNumber(anyString()), times(1));

            verify(repository, times(1)).save(any(CustomerEntity.class));

            assertNotNull(customer);
            assertNotNull(customer.getId());
            assertEquals(ID, customer.getId());
            assertEquals(PHONE_NUMBER, customer.getPhoneNumber());
            assertEquals(1, customer.getAddresses().size());
            assertEquals(EMAIL, customer.getUserRegistrationResponse().getEmail());
            assertEquals(USERNAME, customer.getUserRegistrationResponse().getName());


        }


    }

    @Test
    void testCreateCustomer_WhenCustomerIsInvalid_ShouldThrowInvalidCustomerException() {

        InvalidCustomerException exception = assertThrows(InvalidCustomerException.class, () -> service.create(null));

        assertNotNull(exception);
        assertEquals(InvalidCustomerException.ERROR.formatErrorMessage(INVALID_CUSTOMER_MESSAGE), exception.getMessage());
    }


}
