package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.dto.PasswordDTO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.AddressNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.CustomerNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidPasswordException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.CustomerRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.AddressService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {


    private static final String CUSTOMER_NOT_FOUND_MESSAGE = "This customer was not found, please verify the fields and try again.";

    private static final String ADDRESS_NOT_FOUND_MESSAGE = "Was not found this address associated with this customer," +
            " please verify the fields and try again.";

    private static final String INVALID_PASSWORD_MESSAGE = "The password typed is incorrect," +
            " please verify and try again.";


    private static final String PHONE_NUMBER = "+5511998765432";
    private static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
    private static final String INVALID_ID = "5f68880";
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

    private AddressEntity addressEntity;

    private CustomerEntity customerEntity;

    private PasswordDTO passwordDTO;


    @Mock
    private CustomerRepository repository;

    @Mock
    private AddressService addressService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private CustomerService service;

    @BeforeEach
    public void setUp() {

        addressEntity = new AddressEntity(ID, STREET, CITY, STATE, POSTAL_CODE, COUNTRY);
        addressVO = new AddressVO(ID, STREET, CITY, STATE, POSTAL_CODE, COUNTRY);
        UserEntity userEntity = new UserEntity(ID, USERNAME, EMAIL, PASSWORD, ROLE_NAME);
        customerEntity = new CustomerEntity(ID, PHONE_NUMBER, new HashSet<>(Set.of(addressEntity)), userEntity);
        passwordDTO = new PasswordDTO(PASSWORD);


    }

    @Test
    void testDeleteCustomer_WhenCustomerIsDeleted_ShouldDoNothing() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findCustomerByUserEmail(anyString())).thenReturn(Optional.of(customerEntity));

        when(passwordEncoder.matches(passwordDTO.getPassword(), customerEntity.getUser().getPassword())).thenReturn(true);

        doNothing().when(repository).delete(customerEntity);

        service.delete(passwordDTO);
        verify(repository, times(1)).delete(customerEntity);
        verify(repository, times(1)).delete(any());

        SecurityContextHolder.clearContext();

    }

    @Test
    void testDeleteCustomer_WhenPasswordDontMatchers_ShouldThrowInvalidPasswordException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findCustomerByUserEmail(anyString())).thenReturn(Optional.of(customerEntity));

        when(passwordEncoder.matches(passwordDTO.getPassword(), customerEntity.getUser().getPassword())).thenReturn(false);

        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> service.delete(passwordDTO));

        assertNotNull(exception);
        assertEquals(InvalidPasswordException.ERROR.formatErrorMessage(INVALID_PASSWORD_MESSAGE), exception.getMessage());


        SecurityContextHolder.clearContext();

    }

    @Test
    void testDeleteCustomer_WhenCustomerIsNotFound_ShouldCustomerNotFoundException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);


        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> service.delete(passwordDTO));


        assertNotNull(exception);
        assertEquals(CustomerNotFoundException.ERROR.formatErrorMessage(CUSTOMER_NOT_FOUND_MESSAGE), exception.getMessage());


        SecurityContextHolder.clearContext();

    }


    @Test
    void testAddAddressToCustomer_WhenSuccessful_ShouldReturnAddressObject() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findCustomerByUserEmail(anyString())).thenReturn(Optional.of(customerEntity));
        when(addressService.create(any(AddressVO.class))).thenReturn(addressVO);
        when(repository.save(any(CustomerEntity.class))).thenReturn(customerEntity);


        AddressVO createdAddress = service.addAddressToCustomer(addressVO);

        verify(repository, times(1)).save(any(CustomerEntity.class));
        verify(repository, times(1)).findCustomerByUserEmail(anyString());
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(userDetails).getUsername();

        assertNotNull(createdAddress);
        assertNotNull(createdAddress.getId());
        assertEquals(ID, createdAddress.getId());
        assertEquals(STREET, createdAddress.getStreet());
        assertEquals(CITY, createdAddress.getCity());
        assertEquals(STATE, createdAddress.getState());
        assertEquals(POSTAL_CODE, createdAddress.getPostalCode());
        assertEquals(COUNTRY, createdAddress.getCountry());

        SecurityContextHolder.clearContext();

    }


    @Test
    void testAddAddressToCustomer_WhenCustomerIsNotFound_ShouldThrowCustomerNotFoundException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        SecurityContextHolder.setContext(securityContext);

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> service.addAddressToCustomer(null));

        assertNotNull(exception);
        assertEquals(CustomerNotFoundException.ERROR.formatErrorMessage(CUSTOMER_NOT_FOUND_MESSAGE), exception.getMessage());

        SecurityContextHolder.clearContext();

    }

    @Test
    void testUpdateAddressOfCustomer_WhenSuccessful_ShouldReturnUpdatedAddressObject() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findCustomerByUserEmail(anyString())).thenReturn(Optional.of(customerEntity));
        when(addressService.update(any(AddressVO.class))).thenReturn(addressVO);


        AddressVO updatedAddress = service.updateAddressOfACustomer(addressVO);


        verify(repository, times(1)).findCustomerByUserEmail(anyString());
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(userDetails).getUsername();

        assertNotNull(updatedAddress);
        assertNotNull(updatedAddress.getId());
        assertEquals(ID, updatedAddress.getId());
        assertEquals(STREET, updatedAddress.getStreet());
        assertEquals(CITY, updatedAddress.getCity());
        assertEquals(STATE, updatedAddress.getState());
        assertEquals(POSTAL_CODE, updatedAddress.getPostalCode());
        assertEquals(COUNTRY, updatedAddress.getCountry());

        SecurityContextHolder.clearContext();

    }

    @Test
    void testUpdateAddressOfCustomer_WhenCustomerIsNotFound_ShouldThrowCustomerNotFoundException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        SecurityContextHolder.setContext(securityContext);

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> service.updateAddressOfACustomer(null));

        assertNotNull(exception);
        assertEquals(CustomerNotFoundException.ERROR.formatErrorMessage(CUSTOMER_NOT_FOUND_MESSAGE), exception.getMessage());

        SecurityContextHolder.clearContext();

    }

    @Test
    void testFindAddressOfACustomerByItsId_WhenSuccessful_ShouldReturnUpdatedAddressObject() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findCustomerByUserEmail(anyString())).thenReturn(Optional.of(customerEntity));
        when(addressService.findById(anyString())).thenReturn(addressVO);


        AddressVO addressFounded = service.findAddressOfACustomerByItsId(ID);


        verify(repository, times(1)).findCustomerByUserEmail(anyString());
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(userDetails).getUsername();

        assertNotNull(addressFounded);
        assertNotNull(addressFounded.getId());
        assertEquals(ID, addressFounded.getId());
        assertEquals(STREET, addressFounded.getStreet());
        assertEquals(CITY, addressFounded.getCity());
        assertEquals(STATE, addressFounded.getState());
        assertEquals(POSTAL_CODE, addressFounded.getPostalCode());
        assertEquals(COUNTRY, addressFounded.getCountry());

        SecurityContextHolder.clearContext();

    }

    @Test
    void testFindAddressOfACustomerByItsId_WhenCustomerIsNotFound_ShouldThrowCustomerNotFoundException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        SecurityContextHolder.setContext(securityContext);

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> service.findAddressOfACustomerByItsId(null));

        assertNotNull(exception);
        assertEquals(CustomerNotFoundException.ERROR.formatErrorMessage(CUSTOMER_NOT_FOUND_MESSAGE), exception.getMessage());

        SecurityContextHolder.clearContext();

    }

    @Test
    void testFindAllAddressesOfACustomer_WhenSuccessful_ShouldReturnUpdatedAddressObject() {


        List<AddressEntity> addresses = List.of(addressEntity);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        PageRequest pageRequest = PageRequest.of(0, 10);

        when(repository.findAddressesByCustomerEmail(eq(EMAIL), eq(pageRequest)))
                .thenReturn(new PageImpl<>(addresses));

        Page<AddressVO> allAddressesOfACustomer = service.findAllAddressesOfACustomer(pageRequest);
        AddressVO address = allAddressesOfACustomer.getContent().getFirst();


        verify(repository, times(1)).findAddressesByCustomerEmail(anyString(), any(PageRequest.class));
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(userDetails).getUsername();

        assertNotNull(address);
        assertNotNull(address.getId());
        assertEquals(ID, address.getId());
        assertEquals(STREET, address.getStreet());
        assertEquals(CITY, address.getCity());
        assertEquals(STATE, address.getState());
        assertEquals(POSTAL_CODE, address.getPostalCode());
        assertEquals(COUNTRY, address.getCountry());

        SecurityContextHolder.clearContext();

    }

    @Test
    void testDeleteAddressOfACustomer_WhenAddressWasDeleted_ShouldDoNothing() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findCustomerByUserEmail(anyString())).thenReturn(Optional.of(customerEntity));
        doNothing().when(addressService).delete(anyString());


        service.deleteAddressOfACustomer(ID);


        verify(repository, times(1)).findCustomerByUserEmail(anyString());
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(userDetails).getUsername();


        SecurityContextHolder.clearContext();

    }

    @Test
    void testDeleteAddressOfACustomer_WhenCustomerIsNotFound_ShouldThrowCustomerNotFoundException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        SecurityContextHolder.setContext(securityContext);

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> service.deleteAddressOfACustomer(null));

        assertNotNull(exception);
        assertEquals(CustomerNotFoundException.ERROR.formatErrorMessage(CUSTOMER_NOT_FOUND_MESSAGE), exception.getMessage());

        SecurityContextHolder.clearContext();

    }

    @Test
    void testFindAddressOfACustomerByItsId_WhenAddressIsNotAssociatedWithCostumer_ShouldThrowAddressNotFoundException() throws NoSuchMethodException {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findCustomerByUserEmail(anyString())).thenReturn(Optional.of(customerEntity));

        AddressNotFoundException exception = assertThrows(
                AddressNotFoundException.class, () -> service.findAddressOfACustomerByItsId(INVALID_ID));

        assertNotNull(exception);
        assertEquals(AddressNotFoundException.ERROR.formatErrorMessage(ADDRESS_NOT_FOUND_MESSAGE), exception.getMessage());


    }

    @Test
    void testDeleteAddressOfACustomer_WhenAddressIsNotAssociatedWithCostumer_ShouldThrowAddressNotFoundException() throws NoSuchMethodException {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findCustomerByUserEmail(anyString())).thenReturn(Optional.of(customerEntity));

        AddressNotFoundException exception = assertThrows(
                AddressNotFoundException.class, () -> service.deleteAddressOfACustomer(INVALID_ID));

        assertNotNull(exception);
        assertEquals(AddressNotFoundException.ERROR.formatErrorMessage(ADDRESS_NOT_FOUND_MESSAGE), exception.getMessage());


    }


}
