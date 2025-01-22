package repository;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
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
class UserRepositoryTest extends AbstractionIntegrationTest {


    private static final String EMAIL = "creationuser@example.com";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_CUSTOMER;


    private UserEntity userEntity;


    private final UserRepository repository;

    @Autowired
    UserRepositoryTest(UserRepository userRepository) {

        this.repository = userRepository;
    }


    @BeforeEach
    void setUp() {


        userEntity = new UserEntity(TestConstants.ID, TestConstants.USER_USERNAME,
                EMAIL,TestConstants.USER_PASSWORD, ROLE_NAME);
        repository.save(userEntity);


    }


    @Test
    void testFindCustomerByUserEmail_WhenTheCustomerWasFound_ShouldReturnACustomerObject() {


        UserEntity foundedUser = repository.findUserByEmail(userEntity.getEmail()).orElseThrow(null);

        assertNotNull(foundedUser);
        assertNotNull(foundedUser.getId());
        assertEquals(EMAIL, foundedUser.getEmail());
        assertEquals(TestConstants.USER_USERNAME, foundedUser.getName());
        assertEquals(TestConstants.USER_PASSWORD, foundedUser.getPassword());
        assertEquals(ROLE_NAME, foundedUser.getUserProfile());


    }
}
