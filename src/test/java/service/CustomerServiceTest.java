package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.dto.PasswordDTO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.customer.CustomerNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.InvalidPasswordException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.CustomerRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.address.AddressService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.customer.CustomerService;
import constants.TestConstants;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {


    private static final String CUSTOMER_NOT_FOUND_MESSAGE = "This customer was not found, please verify the fields and try again.";

    private static final String INVALID_PASSWORD_MESSAGE = "The password typed is incorrect," +
            " please verify and try again.";


    private static final String PHONE_NUMBER = "+5511998765432";
    private static final String EMAIL = "user@example.com";

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

        addressVO = new AddressVO(TestConstants.ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);

        addressEntity = new AddressEntity(TestConstants.ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);


        UserEntity userEntity = new UserEntity(TestConstants.ID, TestConstants.USER_USERNAME,
                EMAIL, TestConstants.USER_PASSWORD, ROLE_NAME);

        customerEntity = new CustomerEntity(TestConstants.ID, PHONE_NUMBER, new ArrayList<>(Arrays.asList(addressEntity)), userEntity);
        passwordDTO = new PasswordDTO(TestConstants.USER_PASSWORD);


    }

    @Test
    void testDeleteCustomer_WhenCustomerIsDeleted_ShouldDoNothing() {


        List<AddressEntity> addresses = List.of(addressEntity);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findCustomerByUserEmail(anyString())).thenReturn(Optional.of(customerEntity));

        doNothing().when(addressService).deleteAllAddresses(anyList());

        when(passwordEncoder.matches(passwordDTO.getPassword(), customerEntity.getUser().getPassword())).thenReturn(true);


        PageRequest pageRequest = PageRequest.of(0, 10);

        when(repository.findAddressesByCustomerEmail(eq(EMAIL), eq(pageRequest)))
                .thenReturn(new PageImpl<>(addresses));


        doNothing().when(repository).delete(customerEntity);

        service.delete(passwordDTO);
        verify(repository, times(1)).delete(customerEntity);
        verify(addressService, times(1)).deleteAllAddresses(anyList());

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
        assertEquals(TestConstants.ID, createdAddress.getId());
        assertEquals(TestConstants.ADDRESS_STREET, createdAddress.getStreet());
        assertEquals(TestConstants.ADDRESS_CITY, createdAddress.getCity());
        assertEquals(TestConstants.ADDRESS_STATE, createdAddress.getState());
        assertEquals(TestConstants.ADDRESS_POSTAL_CODE, createdAddress.getPostalCode());
        assertEquals(TestConstants.ADDRESS_COUNTRY, createdAddress.getCountry());

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
        addressVO.setState(TestConstants.ADDRESS_UPDATED_STATE);
        addressVO.setCountry(TestConstants.ADDRESS_UPDATED_COUNTRY);

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
        assertEquals(TestConstants.ID, updatedAddress.getId());
        assertEquals(TestConstants.ADDRESS_STREET, updatedAddress.getStreet());
        assertEquals(TestConstants.ADDRESS_CITY, updatedAddress.getCity());
        assertEquals(TestConstants.ADDRESS_UPDATED_STATE, updatedAddress.getState());
        assertEquals(TestConstants.ADDRESS_POSTAL_CODE, updatedAddress.getPostalCode());
        assertEquals(TestConstants.ADDRESS_UPDATED_COUNTRY, updatedAddress.getCountry());

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


        AddressVO addressFounded = service.findAddressOfACustomerByItsId(TestConstants.ID);


        verify(repository, times(1)).findCustomerByUserEmail(anyString());
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(userDetails).getUsername();

        assertNotNull(addressFounded);
        assertNotNull(addressFounded.getId());
        assertEquals(TestConstants.ID, addressFounded.getId());
        assertEquals(TestConstants.ADDRESS_STREET, addressFounded.getStreet());
        assertEquals(TestConstants.ADDRESS_CITY, addressFounded.getCity());
        assertEquals(TestConstants.ADDRESS_STATE, addressFounded.getState());
        assertEquals(TestConstants.ADDRESS_POSTAL_CODE, addressFounded.getPostalCode());
        assertEquals(TestConstants.ADDRESS_COUNTRY, addressFounded.getCountry());


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
        assertEquals(TestConstants.ID, address.getId());
        assertEquals(TestConstants.ADDRESS_STREET, address.getStreet());
        assertEquals(TestConstants.ADDRESS_CITY, address.getCity());
        assertEquals(TestConstants.ADDRESS_STATE, address.getState());
        assertEquals(TestConstants.ADDRESS_POSTAL_CODE, address.getPostalCode());
        assertEquals(TestConstants.ADDRESS_COUNTRY, address.getCountry());

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


        service.deleteAddressOfACustomer(TestConstants.ID);


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


}
