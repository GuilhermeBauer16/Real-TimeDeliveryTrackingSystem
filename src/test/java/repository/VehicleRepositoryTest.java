package repository;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Status;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Type;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.VehicleRepository;
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
@ContextConfiguration(classes = RealTimeDeliveryTrackingSystemApplication.class) // Specify your main app class here
class VehicleRepositoryTest extends AbstractionIntegrationTest {


    private static final String ID = "d8e7df81-2cd4-41a2-a005-62e6d8079716";
    private static final String NAME = "Voyage";
    private static final String LICENSE_PLATE = "AQE1F34";
    private static final Type TYPE = Type.CAR;
    private static final Status STATUS = Status.AVAILABLE;


    @Autowired
    private VehicleRepository vehicleRepository;


    @BeforeEach
    void setUp() {

        vehicleRepository.save(new VehicleEntity(ID, NAME, LICENSE_PLATE, TYPE, STATUS));

    }

    @Test
    void testFindByLicensePlate_WhenIsSuccessful_ShouldReturnVehicleEntity() {
        VehicleEntity vehicle = vehicleRepository.findByLicensePlate(LICENSE_PLATE).orElse(null);
        assertNotNull(vehicle);
        assertNotNull(vehicle.getId());
        assertEquals(NAME, vehicle.getName());
        assertEquals(LICENSE_PLATE, vehicle.getLicensePlate());
        assertEquals(TYPE, vehicle.getType());
        assertEquals(STATUS, vehicle.getStatus());
    }
}
