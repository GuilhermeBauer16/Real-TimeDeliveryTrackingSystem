package repository;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.DriverEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Status;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Type;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.AddressRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.DriverRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import testContainers.AbstractionIntegrationTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = RealTimeDeliveryTrackingSystemApplication.class)
class DriverRepositoryTest extends AbstractionIntegrationTest {


    private static final String PHONE_NUMBER = "+5511998765432";
    private static final String DRIVER_LICENSE = "12628901031";
    private static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
    private static final String STREET = "123 Main St";
    private static final String CITY = "Sample City";
    private static final String STATE = "Sample State";
    private static final String POSTAL_CODE = "12345";
    private static final String COUNTRY = "Sample Country";

    private static final String VEHICLE_NAME = "Voyage";
    private static final String LICENSE_PLATE = "ABE1F34";
    private static final Type VEHICLE_TYPE = Type.CAR;
    private static final Status VEHICLE_STATUS = Status.AVAILABLE;

    private static final String EMAIL = "driveruser@example.com";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_DRIVER;


    private DriverEntity driverEntity;


    private final DriverRepository repository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    DriverRepositoryTest(DriverRepository repository, AddressRepository addressRepository, UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.repository = repository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }


    @BeforeEach
    void setUp() {

        AddressEntity addressEntity = new AddressEntity(ID, STREET, CITY, STATE, POSTAL_CODE, COUNTRY);
        addressRepository.save(addressEntity);
        UserEntity userEntity = new UserEntity(ID, USERNAME, EMAIL, PASSWORD, ROLE_NAME);
        userRepository.save(userEntity);
        VehicleEntity vehicleEntity = new VehicleEntity(ID, VEHICLE_NAME, LICENSE_PLATE, VEHICLE_TYPE, VEHICLE_STATUS);
        vehicleRepository.save(vehicleEntity);
        driverEntity = new DriverEntity(ID, PHONE_NUMBER, DRIVER_LICENSE, List.of(addressEntity), userEntity, List.of(vehicleEntity));
        repository.save(driverEntity);

    }


    @Test
    void testFindAddressesByDriverEmail_WhenTheAddressesWasFound_ShouldReturnAAddressPageableList() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<AddressEntity> foundedAddresses = repository.findAddressesByDriverEmail(driverEntity.getUser().getEmail(), pageable);
        AddressEntity addressEntity = foundedAddresses.getContent().getFirst();
        assertNotNull(foundedAddresses);
        assertNotNull(addressEntity);
        assertNotNull(addressEntity.getId());
        assertEquals(ID, addressEntity.getId());
        assertEquals(STREET, addressEntity.getStreet());
        assertEquals(CITY, addressEntity.getCity());
        assertEquals(STATE, addressEntity.getState());
        assertEquals(POSTAL_CODE, addressEntity.getPostalCode());
        assertEquals(COUNTRY, addressEntity.getCountry());


    }

    @Test
    void testFindDriverByUserEmail_WhenTheDriverWasFound_ShouldReturnADriverObject() {


        DriverEntity foundedDriver = repository.findDriverByUserEmail(driverEntity.getUser().getEmail()).orElseThrow(null);

        assertNotNull(foundedDriver);
        assertNotNull(foundedDriver.getId());
        assertEquals(ID, foundedDriver.getId());
        assertEquals(PHONE_NUMBER, foundedDriver.getPhoneNumber());
        assertEquals(1, foundedDriver.getAddresses().size());
        assertEquals(EMAIL, foundedDriver.getUser().getEmail());
        assertEquals(USERNAME, foundedDriver.getUser().getName());
        assertEquals(PASSWORD, foundedDriver.getUser().getPassword());
        assertEquals(ROLE_NAME, foundedDriver.getUser().getUserProfile());
        assertEquals(1, foundedDriver.getVehicles().size());


    }

    @Test
    void testFindDriverByDriverLicense_WhenTheDriverWasFound_ShouldReturnADriverObject() {


        DriverEntity foundedDriver = repository.findDriverByDriverLicense(driverEntity.getDriverLicense()).orElseThrow(null);

        assertNotNull(foundedDriver);
        assertNotNull(foundedDriver.getId());
        assertEquals(ID, foundedDriver.getId());
        assertEquals(PHONE_NUMBER, foundedDriver.getPhoneNumber());
        assertEquals(1, foundedDriver.getAddresses().size());
        assertEquals(EMAIL, foundedDriver.getUser().getEmail());
        assertEquals(USERNAME, foundedDriver.getUser().getName());
        assertEquals(PASSWORD, foundedDriver.getUser().getPassword());
        assertEquals(ROLE_NAME, foundedDriver.getUser().getUserProfile());
        assertEquals(1, foundedDriver.getVehicles().size());


    }

    @Test
    void testFindVehicleByDriverEmail_WhenTheDriverWasFound_ShouldReturnAVehicleObject() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<VehicleEntity> vehiclesByDriverEmail = repository.findVehiclesByDriverEmail(driverEntity.getUser().getEmail(), pageable);
        VehicleEntity foundedVehicle = vehiclesByDriverEmail.getContent().getFirst();

        assertNotNull(foundedVehicle);
        assertNotNull(foundedVehicle.getId());
        assertEquals(ID, foundedVehicle.getId());
        assertEquals(VEHICLE_NAME, foundedVehicle.getName());
        assertEquals(LICENSE_PLATE, foundedVehicle.getLicensePlate());
        assertEquals(VEHICLE_TYPE, foundedVehicle.getType());
        assertEquals(VEHICLE_STATUS, foundedVehicle.getStatus());


    }
}
