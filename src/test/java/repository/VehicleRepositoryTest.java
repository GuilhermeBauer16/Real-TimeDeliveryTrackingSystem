package repository;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.VehicleRepository;
import constants.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import testContainers.AbstractionIntegrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = RealTimeDeliveryTrackingSystemApplication.class)
class VehicleRepositoryTest extends AbstractionIntegrationTest {


    private static final String LICENSE_PLATE = "AQE1F34";


    @Autowired
    private VehicleRepository vehicleRepository;


    @BeforeEach
    void setUp() {

        vehicleRepository.save(new VehicleEntity(TestConstants.ID, TestConstants.VEHICLE_NAME,
                LICENSE_PLATE, TestConstants.VEHICLE_TYPE, TestConstants.VEHICLE_STATUS));

    }

    @Test
    void testFindByLicensePlate_WhenIsSuccessful_ShouldReturnVehicleEntity() {
        VehicleEntity vehicle = vehicleRepository.findByLicensePlate(LICENSE_PLATE).orElse(null);
        assertNotNull(vehicle);
        assertNotNull(vehicle.getId());
        assertEquals(TestConstants.VEHICLE_NAME, vehicle.getName());
        assertEquals(LICENSE_PLATE, vehicle.getLicensePlate());
        assertEquals(TestConstants.VEHICLE_TYPE, vehicle.getType());
        assertEquals(TestConstants.VEHICLE_STATUS, vehicle.getStatus());
    }
}
