package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.address.AddressNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.address.InvalidAddressException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.AddressRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.address.AddressService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import constants.TestConstants;
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


    private AddressVO addressVO;
    private AddressEntity addressEntity;

    @Mock
    private AddressRepository repository;

    @InjectMocks
    private AddressService service;

    @BeforeEach
    void setUp() {
        addressVO = new AddressVO(TestConstants.ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);

        addressEntity = new AddressEntity(TestConstants.ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);
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
            assertEquals(TestConstants.ID, createdAddress.getId());
            assertEquals(TestConstants.ADDRESS_STREET, createdAddress.getStreet());
            assertEquals(TestConstants.ADDRESS_CITY, createdAddress.getCity());
            assertEquals(TestConstants.ADDRESS_STATE, createdAddress.getState());
            assertEquals(TestConstants.ADDRESS_POSTAL_CODE, createdAddress.getPostalCode());
            assertEquals(TestConstants.ADDRESS_COUNTRY, createdAddress.getCountry());


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
        assertEquals(TestConstants.ID, address.getId());
        assertEquals(TestConstants.ADDRESS_STREET, address.getStreet());
        assertEquals(TestConstants.ADDRESS_CITY, address.getCity());
        assertEquals(TestConstants.ADDRESS_STATE, address.getState());
        assertEquals(TestConstants.ADDRESS_POSTAL_CODE, address.getPostalCode());
        assertEquals(TestConstants.ADDRESS_COUNTRY, address.getCountry());


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
            addressEntity.setState(TestConstants.ADDRESS_UPDATED_STATE);
            addressEntity.setCountry(TestConstants.ADDRESS_UPDATED_COUNTRY);

            mockedValidatorUtils.when(() -> ValidatorUtils.updateFieldIfNotNull(any(AddressEntity.class), any(), anyString(), any()))
                    .thenAnswer(invocation -> addressEntity);
            when(repository.findById(TestConstants.ID)).thenReturn(Optional.of(addressEntity));
            when(repository.save(any(AddressEntity.class))).thenReturn(addressEntity);

            AddressVO updatedAddress = service.update(addressVO);
            mockedValidatorUtils.verify(() -> ValidatorUtils.updateFieldIfNotNull(any(AddressEntity.class), any(AddressVO.class), anyString(), any()));
            verify(repository, times(1)).findById(anyString());
            verify(repository, times(1)).save(any(AddressEntity.class));

            assertNotNull(updatedAddress);
            assertNotNull(updatedAddress.getId());
            assertEquals(TestConstants.ID, updatedAddress.getId());
            assertEquals(TestConstants.ADDRESS_STREET, updatedAddress.getStreet());
            assertEquals(TestConstants.ADDRESS_CITY, updatedAddress.getCity());
            assertEquals(TestConstants.ADDRESS_UPDATED_STATE, updatedAddress.getState());
            assertEquals(TestConstants.ADDRESS_POSTAL_CODE, updatedAddress.getPostalCode());
            assertEquals(TestConstants.ADDRESS_UPDATED_COUNTRY, updatedAddress.getCountry());


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


        when(repository.findById(TestConstants.ID)).thenReturn(Optional.of(addressEntity));


        AddressVO address = service.findById(TestConstants.ID);

        verify(repository, times(1)).findById(anyString());


        assertNotNull(address);
        assertNotNull(address.getId());
        assertEquals(TestConstants.ID, address.getId());
        assertEquals(TestConstants.ADDRESS_STREET, address.getStreet());
        assertEquals(TestConstants.ADDRESS_CITY, address.getCity());
        assertEquals(TestConstants.ADDRESS_STATE, address.getState());
        assertEquals(TestConstants.ADDRESS_POSTAL_CODE, address.getPostalCode());
        assertEquals(TestConstants.ADDRESS_COUNTRY, address.getCountry());
    }

    @Test
    void testFindAddressById_WhenAddressIsNotFound_ShouldThrowAddressNotFoundException() {

        AddressNotFoundException exception = assertThrows(AddressNotFoundException.class, () -> service.findById(TestConstants.ID));
        assertNotNull(exception);
        assertEquals(AddressNotFoundException.ERROR.formatErrorMessage(ADDRESS_NOT_FOUND_MESSAGE), exception.getMessage());

    }

    @Test
    void testDeleteAddress_WhenSuccessful_ThenDoNothing() {
        when(repository.findById(anyString())).thenReturn(Optional.of(addressEntity));
        doNothing().when(repository).delete(addressEntity);

        service.delete(TestConstants.ID);
        verify(repository, times(1)).findById(anyString());
        verify(repository, times(1)).delete(any(AddressEntity.class));
    }

    @Test
    void testDeleteAddress_WhenAddressIsNotFound_ShouldThrowAddressNotFoundException() {

        AddressNotFoundException exception = assertThrows(AddressNotFoundException.class, () -> service.delete(TestConstants.ID));
        assertNotNull(exception);
        assertEquals(AddressNotFoundException.ERROR.formatErrorMessage(ADDRESS_NOT_FOUND_MESSAGE), exception.getMessage());

    }

    @Test
    void testVerifyIfAddressIdIsAssociatedWithUser_WhenAddressIsNotAssociatedWithUser_ShouldThrowAddressNotFoundException() {

        List<AddressEntity> addresses = List.of(addressEntity);

        AddressNotFoundException exception = assertThrows(AddressNotFoundException.class, () -> service.
                verifyIfAddressIdIsAssociatedWithUser(TestConstants.INVALID_ID, addresses));
        assertNotNull(exception);
        assertEquals(AddressNotFoundException.ERROR.formatErrorMessage(ADDRESS_NOT_ASSOCIATED_MESSAGE), exception.getMessage());
    }

    @Test
    void testVerifyIfAddressIdIsAssociatedWithUser_WhenAddressIsAssociatedWithUser_Return() {

        List<AddressEntity> addresses = List.of(addressEntity);

        assertDoesNotThrow(() -> service.verifyIfAddressIdIsAssociatedWithUser(TestConstants.ID, addresses));
    }


}
