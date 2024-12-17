package repository;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
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
class UserRepositoryTest extends AbstractionIntegrationTest {


    private static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
    private static final String EMAIL = "creationuser@example.com";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_CUSTOMER;


    private UserEntity userEntity;


    private final UserRepository repository;

    @Autowired
    UserRepositoryTest(UserRepository userRepository) {

        this.repository = userRepository;
    }


    @BeforeEach
    void setUp() {


        userEntity = new UserEntity(ID, USERNAME, EMAIL, PASSWORD, ROLE_NAME);
        repository.save(userEntity);


    }


    @Test
    void testFindCustomerByUserEmail_WhenTheCustomerWasFound_ShouldReturnACustomerObject() {


        UserEntity foundedUser = repository.findUserByEmail(userEntity.getEmail()).orElseThrow(null);

        assertNotNull(foundedUser);
        assertNotNull(foundedUser.getId());
        assertEquals(EMAIL, foundedUser.getEmail());
        assertEquals(USERNAME, foundedUser.getName());
        assertEquals(PASSWORD, foundedUser.getPassword());
        assertEquals(ROLE_NAME, foundedUser.getUserProfile());


    }
}
