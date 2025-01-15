package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.address.AddressNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.address.InvalidAddressException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.AddressRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.address.AddressService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    private static final String INVALID_ADDRESS_MESSAGE = "This address is invalid, please verify the fields and try again.";
    private static final String ADDRESS_NOT_FOUND_MESSAGE = "This address can't be find, please verify the fields and try again.";
    private static final String ADDRESS_NOT_ASSOCIATED_MESSAGE = "That address was not associated with this user," +
            " please verify the fields and try again.";

    private static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
    private static final String INVALID_ID = "5f68880";
    private static final String STREET = "123 Main St";
    private static final String CITY = "Sample City";
    private static final String STATE = "Sample State";
    private static final String POSTAL_CODE = "12345";
    private static final String COUNTRY = "Sample Country";

    private static final String UPDATED_COUNTRY = "Brazil";
    private static final String UPDATED_STATE = "Rio de Janeiro";


    private AddressVO addressVO;
    private AddressEntity addressEntity;

    @Mock
    private AddressRepository repository;

    @InjectMocks
    private AddressService service;

    @BeforeEach
    void setUp() {
        addressVO = new AddressVO(ID, STREET, CITY, STATE, POSTAL_CODE, COUNTRY);
        addressEntity = new AddressEntity(ID, STREET, CITY, STATE, POSTAL_CODE, COUNTRY);
    }

    @Test
    void testCreateAddress_WhenSuccess_ShouldReturnAddressObject() {

        try (MockedStatic<ValidatorUtils> mockedValidatorUtils = mockStatic(ValidatorUtils.class)) {

            mockedValidatorUtils.when(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any())).thenAnswer(invocation -> null);
            mockedValidatorUtils.when(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any())).thenAnswer(invocation -> null);

            when(repository.save(any(AddressEntity.class))).thenReturn(addressEntity);


            AddressVO createdAddress = service.create(addressVO);

            mockedValidatorUtils.verify(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any()));
            mockedValidatorUtils.verify(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any()));

            verify(repository, times(1)).save(any(AddressEntity.class));


            assertNotNull(createdAddress);
            assertNotNull(createdAddress.getId());
            assertEquals(ID, createdAddress.getId());
            assertEquals(STREET, createdAddress.getStreet());
            assertEquals(CITY, createdAddress.getCity());
            assertEquals(STATE, createdAddress.getState());
            assertEquals(POSTAL_CODE, createdAddress.getPostalCode());
            assertEquals(COUNTRY, createdAddress.getCountry());


        }


    }

    @Test
    void testCreateAddresses_WhenSuccess_ShouldReturnAddressList() {

        List<AddressEntity> addresses = List.of(addressEntity);

        when(repository.save(any(AddressEntity.class))).thenReturn(addressEntity);

        List<AddressEntity> savedAddresses = service.createAddresses(addresses);
        AddressEntity address = addresses.getFirst();


        assertNotNull(address);
        assertNotNull(address.getId());
        assertEquals(1, savedAddresses.size());
        assertEquals(ID, address.getId());
        assertEquals(STREET, address.getStreet());
        assertEquals(CITY, address.getCity());
        assertEquals(STATE, address.getState());
        assertEquals(POSTAL_CODE, address.getPostalCode());
        assertEquals(COUNTRY, address.getCountry());


    }

    @Test
    void testCreateAddress_WhenAddressIsInvalid_ShouldThrowInvalidAddressException() {

        InvalidAddressException exception = assertThrows(InvalidAddressException.class, () -> service.create(null));
        assertNotNull(exception);
        assertEquals(InvalidAddressException.ERROR.formatErrorMessage(INVALID_ADDRESS_MESSAGE), exception.getMessage());

    }

    @Test
    void testUpdateAddress_WhenSuccess_ShouldReturnUpdatedAddressObject() {

        try (MockedStatic<ValidatorUtils> mockedValidatorUtils = mockStatic(ValidatorUtils.class)) {
            addressEntity.setState(UPDATED_STATE);
            addressEntity.setCountry(UPDATED_COUNTRY);

            mockedValidatorUtils.when(() -> ValidatorUtils.updateFieldIfNotNull(any(AddressEntity.class), any(), anyString(), any()))
                    .thenAnswer(invocation -> addressEntity);
            when(repository.findById(ID)).thenReturn(Optional.of(addressEntity));
            when(repository.save(any(AddressEntity.class))).thenReturn(addressEntity);

            AddressVO updatedAddress = service.update(addressVO);
            mockedValidatorUtils.verify(() -> ValidatorUtils.updateFieldIfNotNull(any(AddressEntity.class), any(AddressVO.class), anyString(), any()));
            verify(repository, times(1)).findById(anyString());
            verify(repository, times(1)).save(any(AddressEntity.class));

            assertNotNull(updatedAddress);
            assertNotNull(updatedAddress.getId());
            assertEquals(ID, updatedAddress.getId());
            assertEquals(STREET, updatedAddress.getStreet());
            assertEquals(CITY, updatedAddress.getCity());
            assertEquals(UPDATED_STATE, updatedAddress.getState());
            assertEquals(POSTAL_CODE, updatedAddress.getPostalCode());
            assertEquals(UPDATED_COUNTRY, updatedAddress.getCountry());


        }
    }

    @Test
    void testUpdateAddress_WhenAddressIsNotFound_ShouldThrowAddressNotFoundException() {

        AddressNotFoundException exception = assertThrows(AddressNotFoundException.class, () -> service.update(addressVO));
        assertNotNull(exception);
        assertEquals(AddressNotFoundException.ERROR.formatErrorMessage(ADDRESS_NOT_FOUND_MESSAGE), exception.getMessage());

    }

    @Test
    void testFindAddressById_WhenSuccess_ShouldReturnAddressObject() {


        when(repository.findById(ID)).thenReturn(Optional.of(addressEntity));


        AddressVO address = service.findById(ID);

        verify(repository, times(1)).findById(anyString());


        assertNotNull(address);
        assertNotNull(address.getId());
        assertEquals(ID, address.getId());
        assertEquals(STREET, address.getStreet());
        assertEquals(CITY, address.getCity());
        assertEquals(STATE, address.getState());
        assertEquals(POSTAL_CODE, address.getPostalCode());
        assertEquals(COUNTRY, address.getCountry());
    }

    @Test
    void testFindAddressById_WhenAddressIsNotFound_ShouldThrowAddressNotFoundException() {

        AddressNotFoundException exception = assertThrows(AddressNotFoundException.class, () -> service.findById(ID));
        assertNotNull(exception);
        assertEquals(AddressNotFoundException.ERROR.formatErrorMessage(ADDRESS_NOT_FOUND_MESSAGE), exception.getMessage());

    }

    @Test
    void testDeleteAddress_WhenSuccessful_ThenDoNothing() {
        when(repository.findById(anyString())).thenReturn(Optional.of(addressEntity));
        doNothing().when(repository).delete(addressEntity);

        service.delete(ID);
        verify(repository, times(1)).findById(anyString());
        verify(repository, times(1)).delete(any(AddressEntity.class));
    }

    @Test
    void testDeleteAddress_WhenAddressIsNotFound_ShouldThrowAddressNotFoundException() {

        AddressNotFoundException exception = assertThrows(AddressNotFoundException.class, () -> service.delete(ID));
        assertNotNull(exception);
        assertEquals(AddressNotFoundException.ERROR.formatErrorMessage(ADDRESS_NOT_FOUND_MESSAGE), exception.getMessage());

    }

    @Test
    void testVerifyIfAddressIdIsAssociatedWithUser_WhenAddressIsNotAssociatedWithUser_ShouldThrowAddressNotFoundException() {

        List<AddressEntity> addresses = List.of(addressEntity);

        AddressNotFoundException exception = assertThrows(AddressNotFoundException.class, () -> service.verifyIfAddressIdIsAssociatedWithUser(INVALID_ID, addresses));
        assertNotNull(exception);
        assertEquals(AddressNotFoundException.ERROR.formatErrorMessage(ADDRESS_NOT_ASSOCIATED_MESSAGE), exception.getMessage());
    }

    @Test
    void testVerifyIfAddressIdIsAssociatedWithUser_WhenAddressIsAssociatedWithUser_Return() {

        List<AddressEntity> addresses = List.of(addressEntity);

        assertDoesNotThrow(() ->  service.verifyIfAddressIdIsAssociatedWithUser(ID, addresses));
    }


}
