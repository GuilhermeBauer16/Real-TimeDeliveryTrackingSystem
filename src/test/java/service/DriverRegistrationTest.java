package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.DriverEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.DriverVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Status;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Type;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.DriverLicenseAllReadyRegisterException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidDriverException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.DriverRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.DriverRegistrationResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.address.AddressService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.driver.DriverRegistrationService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.user.UserRegistrationService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.vehicle.VehicleService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.DriverLicenseValidatorUtils;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.PhoneNumberValidator;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import com.google.i18n.phonenumbers.NumberParseException;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

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
class DriverRegistrationTest {

    private static final String INVALID_DRIVER_MESSAGE = "This Driver is invalid, please verify the fields and try again.";

    private static final String DRIVER_LICENSE_ALREADY_REGISTER_MESSAGE = "This driver license is already registered, " +
            "please verify the fields and try again.";


    private static final String PHONE_NUMBER = "+5511998765432";
    private static final String DRIVER_LICENSE = "14829179653";
    private static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
    private static final String STREET = "123 Main St";
    private static final String CITY = "Sample City";
    private static final String STATE = "Sample State";
    private static final String POSTAL_CODE = "12345";
    private static final String COUNTRY = "Sample Country";

    private static final String VEHICLE_NAME = "Voyage";
    private static final String LICENSE_PLATE = "AQE1F34";
    private static final Type VEHICLE_TYPE = Type.CAR;
    private static final Status VEHICLE_STATUS = Status.AVAILABLE;

    private static final String EMAIL = "user@example.com";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_DRIVER;

    private UserVO userVO;
    private DriverEntity driverEntity;
    private DriverVO driverVO;
    private VehicleVO vehicleVO;

    @Mock
    private VehicleService vehicleService;


    @Mock
    private DriverRepository repository;

    @Mock
    private AddressService addressService;

    @Mock
    private UserRegistrationService userRegistrationService;

    @InjectMocks
    private DriverRegistrationService service;

    @BeforeEach
    public void setUp() {

        AddressEntity addressEntity = new AddressEntity(ID, STREET, CITY, STATE, POSTAL_CODE, COUNTRY);
        UserEntity userEntity = new UserEntity(ID, USERNAME, EMAIL, PASSWORD, ROLE_NAME);
        userVO = new UserVO(ID, USERNAME, EMAIL, PASSWORD, ROLE_NAME);
        VehicleEntity vehicleEntity = new VehicleEntity(ID, VEHICLE_NAME, LICENSE_PLATE, VEHICLE_TYPE, VEHICLE_STATUS);
        vehicleVO = new VehicleVO(ID, VEHICLE_NAME, LICENSE_PLATE, VEHICLE_TYPE, VEHICLE_STATUS);
        driverEntity = new DriverEntity(ID, PHONE_NUMBER, DRIVER_LICENSE, List.of(addressEntity), new ArrayList<>(List.of(vehicleEntity)), userEntity);
        driverVO = new DriverVO(ID, PHONE_NUMBER, DRIVER_LICENSE, List.of(addressEntity), new ArrayList<>(List.of(vehicleEntity)), userEntity);

    }

    @Test
    void testCreateDriver_WhenSuccessful_ShouldReturnDriverObject() throws NumberParseException, MessagingException {

        try (MockedStatic<ValidatorUtils> mockedValidatorUtils = mockStatic(ValidatorUtils.class);
             MockedStatic<PhoneNumberValidator> mockedPhoneNumberValidator = mockStatic(PhoneNumberValidator.class);
             MockedStatic<DriverLicenseValidatorUtils> mockedDriverLicenseValidatorUtils = mockStatic(DriverLicenseValidatorUtils.class)) {

            mockedValidatorUtils.when(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any()))
                    .thenAnswer(invocation -> null);
            mockedValidatorUtils.when(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any()))
                    .thenAnswer(invocation -> null);
            mockedPhoneNumberValidator.when(() -> PhoneNumberValidator.validatePhoneNumber(anyString()))
                    .thenAnswer(invocation -> null);
            mockedDriverLicenseValidatorUtils.when(() -> DriverLicenseValidatorUtils.validateDriverLicense(anyString()))
                    .thenAnswer(invocation -> null);

            when(repository.save(any(DriverEntity.class))).thenReturn(driverEntity);
            when(userRegistrationService.createUser(any(UserVO.class))).thenReturn(userVO);
            when(vehicleService.create(any(VehicleVO.class))).thenReturn(vehicleVO);

            DriverRegistrationResponse driver = service.create(driverVO);

            mockedValidatorUtils.verify(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any()));
            mockedValidatorUtils.verify(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any()));
            mockedPhoneNumberValidator.verify(() -> PhoneNumberValidator.validatePhoneNumber(anyString()), times(1));
            mockedDriverLicenseValidatorUtils.verify(() -> DriverLicenseValidatorUtils.validateDriverLicense(anyString()), times(1));

            verify(repository, times(1)).save(any(DriverEntity.class));

            assertNotNull(driver);
            assertNotNull(driver.getId());
            assertEquals(ID, driver.getId());
            assertEquals(PHONE_NUMBER, driver.getPhoneNumber());
            assertEquals(1, driver.getAddresses().size());
            assertEquals(1, driver.getVehicles().size());
            assertEquals(EMAIL, driver.getUserRegistrationResponse().getEmail());
            assertEquals(USERNAME, driver.getUserRegistrationResponse().getName());


        }


    }

    @Test
    void testCreateDriver_WhenDriverIsInvalid_ShouldThrowInvalidDriverException() {

        InvalidDriverException exception = assertThrows(InvalidDriverException.class, () -> service.create(null));

        assertNotNull(exception);
        assertEquals(InvalidDriverException.ERROR.formatErrorMessage(INVALID_DRIVER_MESSAGE), exception.getMessage());
    }

    @Test
    void testCreateDriver_WhenDriverIsAlreadyRegisterByAnotherDriver_ShouldThrowDriverLicenseAllReadyRegisterException() {


        try (MockedStatic<PhoneNumberValidator> mockedPhoneNumberValidator = mockStatic(PhoneNumberValidator.class)) {
            mockedPhoneNumberValidator.when(() -> PhoneNumberValidator.validatePhoneNumber(anyString()))
                    .thenAnswer(invocation -> null);

            when(repository.findDriverByDriverLicense(DRIVER_LICENSE)).thenReturn(Optional.of(driverEntity));
            DriverLicenseAllReadyRegisterException exception = assertThrows(DriverLicenseAllReadyRegisterException.class, () -> service.create(driverVO));

            assertNotNull(exception);
            assertEquals(DriverLicenseAllReadyRegisterException.ERROR.
                    formatErrorMessage(DRIVER_LICENSE_ALREADY_REGISTER_MESSAGE), exception.getMessage());
        }
    }


}
