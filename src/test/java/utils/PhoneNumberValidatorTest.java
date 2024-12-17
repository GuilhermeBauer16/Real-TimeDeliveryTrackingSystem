package utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidPhoneNumberException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.PhoneNumberValidator;
import com.google.i18n.phonenumbers.NumberParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class PhoneNumberValidatorTest {


    private static final String PHONE_NULL_OR_EMPTY_MESSAGE = "The phone number cannot be null or empty";
    private static final String INVALID_PHONE_MESSAGE = "The phone number %s is invalid. Please verify the fields and try again.";
    private static final String INVALID_PHONE_CHARACTERS_MESSAGE = "Phone number %s contains invalid characters!";

    private static final String PHONE_NUMBER_WITH_PREFIX = "+5511998765432";
    private static final String PHONE_NUMBER_WITHOUT_PREFIX = "1234567890";
    private static final String INVALID_PHONE_NUMBER_WITH_LETTER = "+5511998A65432";
    private static final String INVALID_LONG_PHONE_NUMBER = "+551199876543200000";
    private static final String INVALID_PHONE_NUMBER = "+11998765432";


    @Test
    void testValidPhoneNumber_WhenPhoneNumberIsValid_ShouldDoNothing() {

        assertDoesNotThrow(() -> PhoneNumberValidator.validatePhoneNumber(PHONE_NUMBER_WITH_PREFIX));


    }


    @Test
    void testValidPhoneNumber_WhenPhoneNumberIsNull_ShouldThrowInvalidPhoneNumberException() {

        InvalidPhoneNumberException exception = assertThrows(InvalidPhoneNumberException.class, () -> PhoneNumberValidator.validatePhoneNumber(null));

        assertNotNull(exception);
        assertEquals(InvalidPhoneNumberException.ERROR.formatErrorMessage(PHONE_NULL_OR_EMPTY_MESSAGE), exception.getMessage());
    }

    @Test
    void testValidPhoneNumber_WhenPhoneNumberIsBlank_ShouldThrowInvalidPhoneNumberException() {

        InvalidPhoneNumberException exception = assertThrows(InvalidPhoneNumberException.class, () -> PhoneNumberValidator.validatePhoneNumber(" "));

        assertNotNull(exception);
        assertEquals(InvalidPhoneNumberException.ERROR.formatErrorMessage(PHONE_NULL_OR_EMPTY_MESSAGE), exception.getMessage());
    }

    @Test
    void testValidPhoneNumber_WhenPhoneNumberNotMatchesWithTheRegex_ShouldThrowInvalidPhoneNumberException() {

        InvalidPhoneNumberException exception = assertThrows(InvalidPhoneNumberException.class, () -> PhoneNumberValidator.validatePhoneNumber(INVALID_PHONE_NUMBER_WITH_LETTER));

        assertNotNull(exception);
        assertEquals(InvalidPhoneNumberException.ERROR.formatErrorMessage(
                String.format(INVALID_PHONE_CHARACTERS_MESSAGE, INVALID_PHONE_NUMBER_WITH_LETTER)
        ), exception.getMessage());
    }

    @Test
    void testValidPhoneNumber_WhenPhoneNumberIsInvalidNumber_ShouldThrowInvalidPhoneNumberException(){

        InvalidPhoneNumberException exception = assertThrows(
                InvalidPhoneNumberException.class,
                () -> PhoneNumberValidator.validatePhoneNumber(INVALID_LONG_PHONE_NUMBER)
        );

        assertNotNull(exception);
        assertEquals(InvalidPhoneNumberException.ERROR.formatErrorMessage(
                String.format(INVALID_PHONE_MESSAGE, INVALID_LONG_PHONE_NUMBER)
        ), exception.getMessage());
    }


    @Test
    void validatePhoneNumber_ShouldThrowException_WhenPhoneNumberDoesNotContainPlusSign() {

        NumberParseException exception = assertThrows(
                NumberParseException.class,
                () -> PhoneNumberValidator.validatePhoneNumber(PHONE_NUMBER_WITHOUT_PREFIX)
        );

        assertNotNull(exception);
        assertEquals(String.format(INVALID_PHONE_MESSAGE, PHONE_NUMBER_WITHOUT_PREFIX), exception.getMessage());
    }

    @Test
    void validatePhoneNumber_ShouldThrowException_WhenPhoneNumberIsInvalidButMissingPlus() {

        InvalidPhoneNumberException exception = assertThrows(
                InvalidPhoneNumberException.class,
                () -> PhoneNumberValidator.validatePhoneNumber(INVALID_PHONE_NUMBER)
        );

        assertNotNull(exception);
        assertEquals(InvalidPhoneNumberException.ERROR.formatErrorMessage(
                String.format(INVALID_PHONE_MESSAGE, INVALID_PHONE_NUMBER)
        ), exception.getMessage());
    }


}
