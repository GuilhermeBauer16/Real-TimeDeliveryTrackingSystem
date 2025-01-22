package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.dto.PasswordDTO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.DriverEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.driver.DriverNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.InvalidPasswordException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.vehicle.VehicleNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.DriverRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.VehicleRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.address.AddressService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.driver.DriverService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.vehicle.VehicleService;
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
class DriverServiceTest {


    private static final String DRIVER_NOT_FOUND_MESSAGE = "This driver was not found, please verify the fields and try again.";
    private static final String INVALID_PASSWORD_MESSAGE = "The password typed is incorrect," +
            " please verify and try again.";

    private static final String VEHICLE_NOT_ASSOCIATED_MESSAGE = "That vehicle was not associated with this user," +
            " please verify the fields and try again.";


    private static final String PHONE_NUMBER = "+5511998765432";
    private static final String DRIVER_LICENSE = "14829179653";
    private static final String LICENSE_PLATE = "AQE1F34";
    private static final String UPDATE_LICENSE_PLATE = "AQE1F34";
    private static final String INVALID_LICENSE_PLATE = "AQEF34";
    private static final String EMAIL = "user@example.com";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_DRIVER;

    private AddressVO addressVO;
    private DriverEntity driverEntity;
    private VehicleVO vehicleVO;
    private AddressEntity addressEntity;
    private VehicleEntity vehicleEntity;

    private PasswordDTO passwordDTO;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private DriverRepository repository;

    @Mock
    private AddressService addressService;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private DriverService service;

    @BeforeEach
    public void setUp() {

        addressVO = new AddressVO(TestConstants.ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);

        addressEntity = new AddressEntity(TestConstants.ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);

        UserEntity userEntity = new UserEntity(TestConstants.ID, TestConstants.USER_USERNAME,
                EMAIL, TestConstants.USER_PASSWORD, ROLE_NAME);

        vehicleVO = new VehicleVO(TestConstants.ID, TestConstants.VEHICLE_NAME,
                LICENSE_PLATE, TestConstants.VEHICLE_TYPE, TestConstants.VEHICLE_STATUS);

        vehicleEntity = new VehicleEntity(TestConstants.ID, TestConstants.VEHICLE_NAME,
                LICENSE_PLATE, TestConstants.VEHICLE_TYPE, TestConstants.VEHICLE_STATUS);

        driverEntity = new DriverEntity(TestConstants.ID, PHONE_NUMBER, DRIVER_LICENSE, new ArrayList<>(Arrays.asList(addressEntity))
                , new ArrayList<>(List.of(vehicleEntity)), userEntity);
        passwordDTO = new PasswordDTO(TestConstants.USER_PASSWORD);


    }

    @Test
    void testDeleteDriver_WhenDriverIsDeleted_ShouldDoNothing() {

        List<AddressEntity> addresses = List.of(addressEntity);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);


        when(repository.findDriverByUserEmail(anyString())).thenReturn(Optional.of(driverEntity));


        when(passwordEncoder.matches(passwordDTO.getPassword(), driverEntity.getUser().getPassword())).thenReturn(true);

        PageRequest pageRequest = PageRequest.of(0, 10);

        when(repository.findAddressesByDriverEmail(eq(EMAIL), eq(pageRequest)))
                .thenReturn(new PageImpl<>(addresses));


        doNothing().when(addressService).deleteAllAddresses(anyList());

        List<VehicleEntity> vehiclesEntities = List.of(vehicleEntity);

        when(repository.findVehiclesByDriverEmail(eq(EMAIL), eq(pageRequest))).thenReturn(new PageImpl<>(vehiclesEntities));


        doNothing().when(vehicleService).deleteAllVehicles(anyList());


        service.delete(passwordDTO);


        verify(addressService, times(1)).deleteAllAddresses(anyList());
        verify(vehicleService, times(1)).deleteAllVehicles(anyList());


        verify(repository, times(1)).delete(driverEntity);

        SecurityContextHolder.clearContext();
    }

    @Test
    void testDeleteDriver_WhenPasswordDontMatchers_ShouldThrowInvalidPasswordException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findDriverByUserEmail(anyString())).thenReturn(Optional.of(driverEntity));

        when(passwordEncoder.matches(passwordDTO.getPassword(), driverEntity.getUser().getPassword())).thenReturn(false);

        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> service.delete(passwordDTO));

        assertNotNull(exception);
        assertEquals(InvalidPasswordException.ERROR.formatErrorMessage(INVALID_PASSWORD_MESSAGE), exception.getMessage());


        SecurityContextHolder.clearContext();

    }

    @Test
    void testDeleteDriver_WhenDriverIsNotFound_ShouldDriverNotFoundException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);


        DriverNotFoundException exception = assertThrows(DriverNotFoundException.class, () -> service.delete(passwordDTO));


        assertNotNull(exception);
        assertEquals(DriverNotFoundException.ERROR.formatErrorMessage(DRIVER_NOT_FOUND_MESSAGE), exception.getMessage());


        SecurityContextHolder.clearContext();

    }


    @Test
    void testAddAddressToDriver_WhenSuccessful_ShouldReturnAddressObject() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findDriverByUserEmail(anyString())).thenReturn(Optional.of(driverEntity));
        when(addressService.create(any(AddressVO.class))).thenReturn(addressVO);
        when(repository.save(any(DriverEntity.class))).thenReturn(driverEntity);


        AddressVO createdAddress = service.addAddressToDriver(addressVO);

        verify(repository, times(1)).save(any(DriverEntity.class));
        verify(repository, times(1)).findDriverByUserEmail(anyString());
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
    void testAddAddressToDriver_WhenDriverIsNotFound_ShouldThrowDriverNotFoundException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        SecurityContextHolder.setContext(securityContext);

        DriverNotFoundException exception = assertThrows(DriverNotFoundException.class, () -> service.addAddressToDriver(null));

        assertNotNull(exception);
        assertEquals(DriverNotFoundException.ERROR.formatErrorMessage(DRIVER_NOT_FOUND_MESSAGE), exception.getMessage());

        SecurityContextHolder.clearContext();

    }

    @Test
    void testUpdateAddressOfDriver_WhenSuccessful_ShouldReturnUpdatedAddressObject() {
        addressVO.setState(TestConstants.ADDRESS_UPDATED_STATE);
        addressVO.setCountry(TestConstants.ADDRESS_UPDATED_COUNTRY);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findDriverByUserEmail(anyString())).thenReturn(Optional.of(driverEntity));
        when(addressService.update(any(AddressVO.class))).thenReturn(addressVO);


        AddressVO updatedAddress = service.updateAddressOfADriver(addressVO);


        verify(repository, times(1)).findDriverByUserEmail(anyString());
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
    void testUpdateAddressOfDriver_WhenDriverIsNotFound_ShouldThrowCustomerNotFoundException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        SecurityContextHolder.setContext(securityContext);

        DriverNotFoundException exception = assertThrows(DriverNotFoundException.class, () -> service.updateAddressOfADriver(null));

        assertNotNull(exception);
        assertEquals(DriverNotFoundException.ERROR.formatErrorMessage(DRIVER_NOT_FOUND_MESSAGE), exception.getMessage());

        SecurityContextHolder.clearContext();

    }

    @Test
    void testFindAddressOfADriverByItsId_WhenSuccessful_ShouldReturnAddressObject() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findDriverByUserEmail(anyString())).thenReturn(Optional.of(driverEntity));
        when(addressService.findById(anyString())).thenReturn(addressVO);


        AddressVO addressFounded = service.findAddressOfADriverByItsId(TestConstants.ID);


        verify(repository, times(1)).findDriverByUserEmail(anyString());
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
    void testFindAddressOfADriverByItsId_WhenDriverIsNotFound_ShouldThrowDriverNotFoundException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        SecurityContextHolder.setContext(securityContext);

        DriverNotFoundException exception = assertThrows(DriverNotFoundException.class, () -> service.findAddressOfADriverByItsId(null));

        assertNotNull(exception);
        assertEquals(DriverNotFoundException.ERROR.formatErrorMessage(DRIVER_NOT_FOUND_MESSAGE), exception.getMessage());

        SecurityContextHolder.clearContext();

    }

    @Test
    void testFindAllAddressesOfADriver_WhenSuccessful_ShouldReturnDriverAddresses() {


        List<AddressEntity> addresses = List.of(addressEntity);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        PageRequest pageRequest = PageRequest.of(0, 10);

        when(repository.findAddressesByDriverEmail(eq(EMAIL), eq(pageRequest)))
                .thenReturn(new PageImpl<>(addresses));

        Page<AddressVO> allAddressesOfDriver = service.findAllAddressesOfADriver(pageRequest);
        AddressVO address = allAddressesOfDriver.getContent().getFirst();


        verify(repository, times(1)).findAddressesByDriverEmail(anyString(), any(PageRequest.class));
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
    void testDeleteAddressOfADriver_WhenAddressWasDeleted_ShouldDoNothing() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findDriverByUserEmail(anyString())).thenReturn(Optional.of(driverEntity));
        doNothing().when(addressService).delete(anyString());


        service.deleteAddressOfADriver(TestConstants.ID);


        verify(repository, times(1)).findDriverByUserEmail(anyString());
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(userDetails).getUsername();


        SecurityContextHolder.clearContext();

    }

    @Test
    void testDeleteAddressOfADriver_WhenDriverIsNotFound_ShouldThrowDriverNotFoundException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        SecurityContextHolder.setContext(securityContext);

        DriverNotFoundException exception = assertThrows(DriverNotFoundException.class, () -> service.deleteAddressOfADriver(null));

        assertNotNull(exception);
        assertEquals(DriverNotFoundException.ERROR.formatErrorMessage(DRIVER_NOT_FOUND_MESSAGE), exception.getMessage());

        SecurityContextHolder.clearContext();

    }

    @Test
    void testCreateVehicleToDriver_WhenSuccessful_ShouldReturnVehicleObject() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findDriverByUserEmail(anyString())).thenReturn(Optional.of(driverEntity));
        when(vehicleService.create(any(VehicleVO.class))).thenReturn(vehicleVO);
        when(repository.save(any(DriverEntity.class))).thenReturn(driverEntity);


        VehicleVO vehicle = service.createVehicle(vehicleVO);

        verify(repository, times(1)).save(any(DriverEntity.class));
        verify(repository, times(1)).findDriverByUserEmail(anyString());
        verify(vehicleService, times(1)).create(any(VehicleVO.class));
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(userDetails).getUsername();

        assertNotNull(vehicle);
        assertNotNull(vehicle.getId());
        assertEquals(TestConstants.ID, vehicle.getId());
        assertEquals(TestConstants.VEHICLE_NAME, vehicle.getName());
        assertEquals(LICENSE_PLATE, vehicle.getLicensePlate());
        assertEquals(TestConstants.VEHICLE_TYPE, vehicle.getType());
        assertEquals(TestConstants.VEHICLE_STATUS, vehicle.getStatus());


        SecurityContextHolder.clearContext();

    }

    @Test
    void testCreateVehicleToDriver_WhenDriverIsNotFound_ShouldThrowDriverNotFoundException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        SecurityContextHolder.setContext(securityContext);

        DriverNotFoundException exception = assertThrows(DriverNotFoundException.class, () -> service.createVehicle(null));

        assertNotNull(exception);
        assertEquals(DriverNotFoundException.ERROR.formatErrorMessage(DRIVER_NOT_FOUND_MESSAGE), exception.getMessage());

        SecurityContextHolder.clearContext();

    }

    @Test
    void testUpdateVehicle_WhenSuccessful_ShouldReturnVehicleObject() {

        vehicleVO.setLicensePlate(UPDATE_LICENSE_PLATE);
        vehicleVO.setName(TestConstants.VEHICLE_UPDATED_NAME);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        SecurityContextHolder.setContext(securityContext);

        when(repository.findDriverByUserEmail(anyString())).thenReturn(Optional.of(driverEntity));
        when(vehicleService.update(any(VehicleVO.class))).thenReturn(vehicleVO);

        VehicleVO vehicle = service.updateVehicle(vehicleVO);

        verify(repository, times(1)).findDriverByUserEmail(anyString());
        verify(vehicleService, times(1)).update(any(VehicleVO.class));
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(userDetails).getUsername();

        assertNotNull(vehicle);
        assertNotNull(vehicle.getId());
        assertEquals(TestConstants.ID, vehicle.getId());
        assertEquals(TestConstants.VEHICLE_UPDATED_NAME, vehicle.getName());
        assertEquals(UPDATE_LICENSE_PLATE, vehicle.getLicensePlate());
        assertEquals(TestConstants.VEHICLE_TYPE, vehicle.getType());
        assertEquals(TestConstants.VEHICLE_STATUS, vehicle.getStatus());

    }


    @Test
    void testUpdateVehicle_WhenVehicleIsNotAssociatedWithDriver_ShouldThrowVehicleNotFoundException() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findDriverByUserEmail(anyString())).thenReturn(Optional.of(driverEntity));

        vehicleVO.setId(TestConstants.INVALID_ID);
        VehicleNotFoundException exception = assertThrows(
                VehicleNotFoundException.class, () -> service.updateVehicle(vehicleVO));

        assertNotNull(exception);
        assertEquals(VehicleNotFoundException.ERROR.formatErrorMessage(VEHICLE_NOT_ASSOCIATED_MESSAGE), exception.getMessage());


    }

    @Test
    void testUpdateVehicleToDriver_WhenDriverIsNotFound_ShouldThrowDriverNotFoundException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);


        SecurityContextHolder.setContext(securityContext);

        DriverNotFoundException exception = assertThrows(DriverNotFoundException.class, () -> service.updateVehicle(null));

        assertNotNull(exception);
        assertEquals(DriverNotFoundException.ERROR.formatErrorMessage(DRIVER_NOT_FOUND_MESSAGE), exception.getMessage());


    }

    @Test
    void testFindVehicleById_WhenSuccessful_ShouldReturnVehicleObject() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        SecurityContextHolder.setContext(securityContext);

        when(repository.findDriverByUserEmail(anyString())).thenReturn(Optional.of(driverEntity));
        when(vehicleService.findById(anyString())).thenReturn(vehicleVO);

        VehicleVO vehicle = service.findVehicleById(TestConstants.ID);

        verify(repository, times(1)).findDriverByUserEmail(anyString());
        verify(vehicleService, times(1)).findById(anyString());
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(userDetails).getUsername();

        assertNotNull(vehicle);
        assertNotNull(vehicle.getId());
        assertEquals(TestConstants.ID, vehicle.getId());
        assertEquals(TestConstants.VEHICLE_NAME, vehicle.getName());
        assertEquals(LICENSE_PLATE, vehicle.getLicensePlate());
        assertEquals(TestConstants.VEHICLE_TYPE, vehicle.getType());
        assertEquals(TestConstants.VEHICLE_STATUS, vehicle.getStatus());


    }

    @Test
    void testFindVehicleById_WhenVehicleIsNotAssociatedWithDriver_ShouldThrowVehicleNotFoundException() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findDriverByUserEmail(anyString())).thenReturn(Optional.of(driverEntity));

        VehicleNotFoundException exception = assertThrows(
                VehicleNotFoundException.class, () -> service.findVehicleById(TestConstants.INVALID_ID));

        assertNotNull(exception);
        assertEquals(VehicleNotFoundException.ERROR.formatErrorMessage(VEHICLE_NOT_ASSOCIATED_MESSAGE), exception.getMessage());


    }

    @Test
    void testFindVehicleById_WhenDriverIsNotFound_ShouldThrowDriverNotFoundException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        SecurityContextHolder.setContext(securityContext);

        DriverNotFoundException exception = assertThrows(DriverNotFoundException.class, () -> service.findVehicleById(null));

        assertNotNull(exception);
        assertEquals(DriverNotFoundException.ERROR.formatErrorMessage(DRIVER_NOT_FOUND_MESSAGE), exception.getMessage());


    }

    @Test
    void testFindVehicleByLicensePlate_WhenSuccessful_ShouldReturnVehicleObject() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        SecurityContextHolder.setContext(securityContext);

        when(repository.findDriverByUserEmail(anyString())).thenReturn(Optional.of(driverEntity));
        when(vehicleService.findByLicensePlate(anyString())).thenReturn(vehicleVO);

        VehicleVO vehicle = service.findVehicleByLicensePlate(LICENSE_PLATE);

        verify(repository, times(1)).findDriverByUserEmail(anyString());
        verify(vehicleService, times(1)).findByLicensePlate(anyString());
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(userDetails).getUsername();

        assertNotNull(vehicle);
        assertNotNull(vehicle.getId());
        assertEquals(TestConstants.ID, vehicle.getId());
        assertEquals(TestConstants.VEHICLE_NAME, vehicle.getName());
        assertEquals(LICENSE_PLATE, vehicle.getLicensePlate());
        assertEquals(TestConstants.VEHICLE_TYPE, vehicle.getType());
        assertEquals(TestConstants.VEHICLE_STATUS, vehicle.getStatus());

    }

    @Test
    void testFindVehicleByLicensePlate_WhenVehicleIsNotAssociatedWithDriver_ShouldThrowVehicleNotFoundException() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findDriverByUserEmail(anyString())).thenReturn(Optional.of(driverEntity));

        VehicleNotFoundException exception = assertThrows(
                VehicleNotFoundException.class, () -> service.findVehicleByLicensePlate(INVALID_LICENSE_PLATE));

        assertNotNull(exception);
        assertEquals(VehicleNotFoundException.ERROR.formatErrorMessage(VEHICLE_NOT_ASSOCIATED_MESSAGE), exception.getMessage());


    }

    @Test
    void testFindVehicleByLicensePlate_WhenDriverIsNotFound_ShouldThrowDriverNotFoundException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        SecurityContextHolder.setContext(securityContext);

        DriverNotFoundException exception = assertThrows(DriverNotFoundException.class, () -> service.findVehicleByLicensePlate(null));

        assertNotNull(exception);
        assertEquals(DriverNotFoundException.ERROR.formatErrorMessage(DRIVER_NOT_FOUND_MESSAGE), exception.getMessage());


    }

    @Test
    void testFindAllVehiclesOfADriver_WhenSuccessful_ShouldReturnDriverVehicles() {


        List<VehicleEntity> vehicles = List.of(vehicleEntity);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        PageRequest pageRequest = PageRequest.of(0, 10);

        when(repository.findVehiclesByDriverEmail(eq(EMAIL), eq(pageRequest)))
                .thenReturn(new PageImpl<>(vehicles));

        Page<VehicleVO> allVehiclesOfADriver = service.findAllVehicles(pageRequest);
        VehicleVO vehicle = allVehiclesOfADriver.getContent().getFirst();


        verify(repository, times(1)).findVehiclesByDriverEmail(anyString(), any(PageRequest.class));
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(userDetails).getUsername();

        assertNotNull(vehicle);
        assertNotNull(vehicle.getId());
        assertEquals(TestConstants.ID, vehicle.getId());
        assertEquals(TestConstants.VEHICLE_NAME, vehicle.getName());
        assertEquals(LICENSE_PLATE, vehicle.getLicensePlate());
        assertEquals(TestConstants.VEHICLE_TYPE, vehicle.getType());
        assertEquals(TestConstants.VEHICLE_STATUS, vehicle.getStatus());


    }

    @Test
    void testDeleteVehicleById_WhenSuccessful_ShouldDoNothing() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findDriverByUserEmail(anyString())).thenReturn(Optional.of(driverEntity));
        doNothing().when(vehicleService).delete(anyString());


        service.deleteVehicle(TestConstants.ID);

        verify(vehicleService, times(1)).delete(anyString());
        verify(repository, times(1)).findDriverByUserEmail(anyString());

    }

    @Test
    void testDeleteVehicleById_WhenVehicleIsNotAssociatedWithDriver_ShouldThrowVehicleNotFoundException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        SecurityContextHolder.setContext(securityContext);

        when(repository.findDriverByUserEmail(anyString())).thenReturn(Optional.of(driverEntity));

        VehicleNotFoundException exception = assertThrows(
                VehicleNotFoundException.class, () -> service.deleteVehicle(TestConstants.INVALID_ID));

        assertNotNull(exception);
        assertEquals(VehicleNotFoundException.ERROR.formatErrorMessage(VEHICLE_NOT_ASSOCIATED_MESSAGE), exception.getMessage());


    }

    @Test
    void testDeleteVehicleById_WhenDriverIsNotFound_ShouldThrowDriverNotFoundException() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        SecurityContextHolder.setContext(securityContext);

        DriverNotFoundException exception = assertThrows(DriverNotFoundException.class, () -> service.deleteVehicle(null));

        assertNotNull(exception);
        assertEquals(DriverNotFoundException.ERROR.formatErrorMessage(DRIVER_NOT_FOUND_MESSAGE), exception.getMessage());


    }

}
