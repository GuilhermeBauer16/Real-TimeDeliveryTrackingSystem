package service;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.user.JwtDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtDetailsServiceTest {

    private static final String USER_NOT_FOUND_MESSAGE = "An User with that email %s was not found!";

    private static final String EMAIL = "user@example.com";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_DRIVER;


    @Mock
    private UserEntity userEntity;

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private JwtDetailsService jwtDetailsService;

    @BeforeEach
    public void setUp() {

        userEntity = new UserEntity(ID,USERNAME,EMAIL,PASSWORD,ROLE_NAME);
    }

    @Test
    void testLoadUserByUsername_WhenUserExists_ShouldReturnUserEntity() {

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = jwtDetailsService.loadUserByUsername(EMAIL);

        verify(userRepository, times(1)).findUserByEmail(EMAIL);
        assertNotNull(userDetails);
        assertEquals(EMAIL,userDetails.getUsername());
        assertEquals(PASSWORD,userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(
                grantedAuthority -> grantedAuthority.getAuthority().equals(ROLE_NAME.name())));

    }

    @Test
    void testLoadUserByUsername_WhenUserDoesNotExist_ShouldThrowUsernameNotFoundException() {

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> jwtDetailsService.loadUserByUsername(EMAIL));
        assertNotNull(exception);
        assertEquals(exception.getMessage(),String.format(USER_NOT_FOUND_MESSAGE, EMAIL));
    }
}
