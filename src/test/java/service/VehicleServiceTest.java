package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Status;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Type;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.driver.DuplicatedLicensePlateException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.vehicle.VehicleNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.VehicleRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.vehicle.VehicleService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
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
class VehicleServiceTest {

    private static final String VEHICLE_NOT_FOUND = "These Vehicle was Not Found";

    private static final String DUPLICATED_LICENSE_PLATE_MESSAGE = "That license plate already registered in the system. " +
            "Please verify the license plate and try again!.";

    private static final String ID = "d8e7df81-2cd4-41a2-a005-62e6d8079716";
    private static final String NAME = "Voyage";
    private static final String LICENSE_PLATE = "AQE1F34";
    private static final Type TYPE = Type.CAR;
    private static final Status STATUS = Status.AVAILABLE;

    private static final String UPDATED_NAME = "Gol";
    private static final String UPDATED_LICENSE_PLATE = "AXE1F34";

    @Mock
    private VehicleRepository repository;

    @Mock
    private VehicleVO vehicleVO;

    @Mock
    private VehicleEntity vehicleEntity;

    @InjectMocks
    private VehicleService service;

    @BeforeEach
    void setUp() {
        vehicleVO = new VehicleVO(ID, NAME, LICENSE_PLATE, TYPE, STATUS);
        vehicleEntity = new VehicleEntity(ID, NAME, LICENSE_PLATE, TYPE, STATUS);
    }

    @Test
    void testCreateVehicle_WhenSuccessful_ShouldReturnVehicleVOObject() {

        try (MockedStatic<ValidatorUtils> mockedValidatorUtils = mockStatic(ValidatorUtils.class)) {
            mockedValidatorUtils.when(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any())).thenAnswer(invocation -> null);
            mockedValidatorUtils.when(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any())).thenAnswer(invocation -> null);
            when(repository.save(any(VehicleEntity.class))).thenReturn(vehicleEntity);

            VehicleVO savedVehicle = service.create(vehicleVO);
            mockedValidatorUtils.verify(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any()));
            mockedValidatorUtils.verify(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any()));
            verify(repository, times(1)).save(any(VehicleEntity.class));

            assertNotNull(savedVehicle);
            assertNotNull(savedVehicle.getId());
            assertEquals(ID, savedVehicle.getId());
            assertEquals(NAME, savedVehicle.getName());
            assertEquals(LICENSE_PLATE, savedVehicle.getLicensePlate());
            assertEquals(TYPE, savedVehicle.getType());
            assertEquals(STATUS, savedVehicle.getStatus());


        }

    }

    @Test
    void testCreateVehicle_WhenVehicleNotFound_ShouldThrowVehicleNotFoundException() {

        VehicleNotFoundException exception = assertThrows(VehicleNotFoundException.class, () -> service.create(null));
        assertNotNull(exception);
        assertEquals(VehicleNotFoundException.ERROR.formatErrorMessage(VEHICLE_NOT_FOUND), exception.getMessage());

    }

    @Test
    void testCreateVehicle_WhenLicensePlateIsADuplicatedValue_ShouldThrowDuplicatedLicensePlateException() {

        when(repository.findByLicensePlate(any(String.class))).thenReturn(Optional.of(vehicleEntity));
        DuplicatedLicensePlateException exception = assertThrows(DuplicatedLicensePlateException.class, () -> service.create(vehicleVO));
        assertNotNull(exception);
        assertEquals(DuplicatedLicensePlateException.ERROR.formatErrorMessage(DUPLICATED_LICENSE_PLATE_MESSAGE), exception.getMessage());

    }

    @Test
    void testUpdateVehicle_WhenSuccessful_ShouldReturnUpdatedVehicleVOObject() {

        try (MockedStatic<ValidatorUtils> mockedValidatorUtils = mockStatic(ValidatorUtils.class)) {
            vehicleEntity.setLicensePlate(UPDATED_LICENSE_PLATE);
            vehicleEntity.setName(UPDATED_NAME);

            mockedValidatorUtils.when(() -> ValidatorUtils.updateFieldIfNotNull(any(VehicleEntity.class), any(), anyString(), any()))
                    .thenAnswer(invocation -> vehicleEntity);
            when(repository.findById(ID)).thenReturn(Optional.of(vehicleEntity));
            when(repository.save(any(VehicleEntity.class))).thenReturn(vehicleEntity);

            VehicleVO updatedVehicle = service.update(vehicleVO);
            mockedValidatorUtils.verify(() -> ValidatorUtils.updateFieldIfNotNull(any(VehicleEntity.class), any(VehicleVO.class), anyString(), any()));
            verify(repository, times(1)).findById(anyString());
            verify(repository, times(1)).save(any(VehicleEntity.class));

            assertNotNull(updatedVehicle);
            assertNotNull(updatedVehicle.getId());
            assertEquals(ID, updatedVehicle.getId());
            assertEquals(UPDATED_NAME, updatedVehicle.getName());
            assertEquals(UPDATED_LICENSE_PLATE, updatedVehicle.getLicensePlate());


        }
    }

    @Test
    void testUpdatedVehicle_WhenVehicleNotFound_ShouldThrowVehicleNotFoundException() {

        VehicleNotFoundException exception = assertThrows(VehicleNotFoundException.class, () -> service.update(vehicleVO));
        assertNotNull(exception);
        assertEquals(VehicleNotFoundException.ERROR.formatErrorMessage(VEHICLE_NOT_FOUND), exception.getMessage());

    }

    @Test
    void testFindVehicleById_WhenSuccessful_ShouldReturnVehicleVOObject() {

        when(repository.findById(anyString())).thenReturn(Optional.of(vehicleEntity));

        VehicleVO vehicleRetrieved = service.findById(ID);

        verify(repository, times(1)).findById(anyString());

        assertNotNull(vehicleRetrieved);
        assertNotNull(vehicleRetrieved.getId());
        assertEquals(ID, vehicleRetrieved.getId());
        assertEquals(NAME, vehicleRetrieved.getName());
        assertEquals(LICENSE_PLATE, vehicleRetrieved.getLicensePlate());
        assertEquals(TYPE, vehicleRetrieved.getType());
        assertEquals(STATUS, vehicleRetrieved.getStatus());

    }

    @Test
    void testFindVehicleById_WhenVehicleNotFound_ShouldThrowVehicleNotFoundException() {

        VehicleNotFoundException exception = assertThrows(VehicleNotFoundException.class, () -> service.findById(ID));
        assertNotNull(exception);
        assertEquals(VehicleNotFoundException.ERROR.formatErrorMessage(VEHICLE_NOT_FOUND), exception.getMessage());

    }

    @Test
    void testFindVehicleByLicensePlate_WhenSuccessful_ShouldReturnVehicleVOObject() {

        when(repository.findByLicensePlate(anyString())).thenReturn(Optional.of(vehicleEntity));

        VehicleVO vehicleRetrieved = service.findByLicensePlate(LICENSE_PLATE);

        verify(repository, times(1)).findByLicensePlate(anyString());

        assertNotNull(vehicleRetrieved);
        assertNotNull(vehicleRetrieved.getId());
        assertEquals(ID, vehicleRetrieved.getId());
        assertEquals(NAME, vehicleRetrieved.getName());
        assertEquals(LICENSE_PLATE, vehicleRetrieved.getLicensePlate());
        assertEquals(TYPE, vehicleRetrieved.getType());
        assertEquals(STATUS, vehicleRetrieved.getStatus());

    }

    @Test
    void testFindVehicleByLicensePlate_WhenVehicleNotFound_ShouldThrowVehicleNotFoundException() {

        VehicleNotFoundException exception = assertThrows(VehicleNotFoundException.class, () -> service.findByLicensePlate(LICENSE_PLATE));
        assertNotNull(exception);
        assertEquals(VehicleNotFoundException.ERROR.formatErrorMessage(VEHICLE_NOT_FOUND), exception.getMessage());

    }

    @Test
    void testFindAllVehicles_WhenSuccessful_ShouldReturnPaginatedVehicles() {

        List<VehicleEntity> vehiclesEntities = List.of(vehicleEntity);
        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(vehiclesEntities));
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<VehicleVO> vehicles = service.findAll(pageRequest);
        verify(repository, times(1)).findAll(any(Pageable.class));

        VehicleVO vehicleRetrieved = vehicles.getContent().getFirst();

        assertNotNull(vehicleRetrieved);
        assertNotNull(vehicleRetrieved.getId());
        assertEquals(1, vehicles.getContent().size());
        assertNotNull(vehicleRetrieved.getId());
        assertEquals(ID, vehicleRetrieved.getId());
        assertEquals(NAME, vehicleRetrieved.getName());
        assertEquals(LICENSE_PLATE, vehicleRetrieved.getLicensePlate());
        assertEquals(TYPE, vehicleRetrieved.getType());
        assertEquals(STATUS, vehicleRetrieved.getStatus());

    }

    @Test
    void testDeleteVehicle_WhenSuccessful_ThenDoNothing() {
        when(repository.findById(anyString())).thenReturn(Optional.of(vehicleEntity));
        doNothing().when(repository).delete(vehicleEntity);

        service.delete(ID);
        verify(repository, times(1)).findById(anyString());
        verify(repository, times(1)).delete(any(VehicleEntity.class));
    }

    @Test
    void testDeleteVehicle_WhenVehicleNotFound_ShouldThrowVehicleNotFoundException() {

        VehicleNotFoundException exception = assertThrows(VehicleNotFoundException.class, () -> service.delete(ID));
        assertNotNull(exception);
        assertEquals(VehicleNotFoundException.ERROR.formatErrorMessage(VEHICLE_NOT_FOUND), exception.getMessage());

    }
}

