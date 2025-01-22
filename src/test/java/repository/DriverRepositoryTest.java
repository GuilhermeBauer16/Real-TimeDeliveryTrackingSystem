package repository;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.DriverEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.AddressRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.DriverRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.VehicleRepository;
import constants.TestConstants;
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

    private static final String LICENSE_PLATE = "ABE1F34";
    private static final String EMAIL = "driveruser@example.com";
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

        AddressEntity addressEntity = new AddressEntity(TestConstants.ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);

        UserEntity userEntity = new UserEntity(TestConstants.ID, TestConstants.USER_USERNAME,
                EMAIL,TestConstants.USER_PASSWORD, ROLE_NAME);

        addressRepository.save(addressEntity);
        userRepository.save(userEntity);

        VehicleEntity vehicleEntity = new VehicleEntity(TestConstants.ID, TestConstants.VEHICLE_NAME,
                LICENSE_PLATE, TestConstants.VEHICLE_TYPE, TestConstants.VEHICLE_STATUS);

        vehicleRepository.save(vehicleEntity);

        driverEntity = new DriverEntity(TestConstants.ID, PHONE_NUMBER, DRIVER_LICENSE, List.of(addressEntity), List.of(vehicleEntity), userEntity);
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
        assertEquals(TestConstants.ID, addressEntity.getId());
        assertEquals(TestConstants.ADDRESS_STREET, addressEntity.getStreet());
        assertEquals(TestConstants.ADDRESS_CITY, addressEntity.getCity());
        assertEquals(TestConstants.ADDRESS_STATE, addressEntity.getState());
        assertEquals(TestConstants.ADDRESS_POSTAL_CODE, addressEntity.getPostalCode());
        assertEquals(TestConstants.ADDRESS_COUNTRY, addressEntity.getCountry());


    }

    @Test
    void testFindDriverByUserEmail_WhenTheDriverWasFound_ShouldReturnADriverObject() {


        DriverEntity foundedDriver = repository.findDriverByUserEmail(driverEntity.getUser().getEmail()).orElseThrow(null);

        assertNotNull(foundedDriver);
        assertNotNull(foundedDriver.getId());
        assertEquals(TestConstants.ID, foundedDriver.getId());
        assertEquals(PHONE_NUMBER, foundedDriver.getPhoneNumber());
        assertEquals(1, foundedDriver.getAddresses().size());
        assertEquals(EMAIL, foundedDriver.getUser().getEmail());
        assertEquals(TestConstants.USER_USERNAME, foundedDriver.getUser().getName());
        assertEquals(TestConstants.USER_PASSWORD, foundedDriver.getUser().getPassword());
        assertEquals(ROLE_NAME, foundedDriver.getUser().getUserProfile());
        assertEquals(1, foundedDriver.getVehicles().size());


    }

    @Test
    void testFindDriverByDriverLicense_WhenTheDriverWasFound_ShouldReturnADriverObject() {


        DriverEntity foundedDriver = repository.findDriverByDriverLicense(driverEntity.getDriverLicense()).orElseThrow(null);

        assertNotNull(foundedDriver);
        assertNotNull(foundedDriver.getId());
        assertEquals(TestConstants.ID, foundedDriver.getId());
        assertEquals(PHONE_NUMBER, foundedDriver.getPhoneNumber());
        assertEquals(1, foundedDriver.getAddresses().size());
        assertEquals(EMAIL, foundedDriver.getUser().getEmail());
        assertEquals(TestConstants.USER_USERNAME, foundedDriver.getUser().getName());
        assertEquals(TestConstants.USER_PASSWORD, foundedDriver.getUser().getPassword());
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
        assertEquals(TestConstants.ID, foundedVehicle.getId());
        assertEquals(TestConstants.VEHICLE_NAME, foundedVehicle.getName());
        assertEquals(LICENSE_PLATE, foundedVehicle.getLicensePlate());
        assertEquals(TestConstants.VEHICLE_TYPE, foundedVehicle.getType());
        assertEquals(TestConstants.VEHICLE_STATUS, foundedVehicle.getStatus());


    }
}
