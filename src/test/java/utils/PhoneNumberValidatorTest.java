package utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidPhoneNumberException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.PhoneNumberValidator;
import com.google.i18n.phonenumbers.NumberParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
public class PhoneNumberValidatorTest {


    private static final String PHONE_NULL_OR_EMPTY_MESSAGE = "The phone number cannot be null or empty";
    private static final String INVALID_PHONE_MESSAGE = "The phone number %s is invalid. Please verify the fields and try again.";
    private static final String INVALID_PHONE_CHARACTERS_MESSAGE = "Phone number %s contains invalid characters!";

    private static final String PHONE_NUMBER_WITH_PREFIX = "+5511998765432";
    private static final String PHONE_NUMBER_WITHOUT_PREFIX = "5511998765432";
    private static final String INVALID_PHONE_NUMBER_WITH_LETTER = "+5511998A65432";
    private static final String INVALID_PHONE_NUMBER = "+551199832";


    @Test
    void testValidPhoneNumber_WhenPhoneNumberIsValid_ShouldDoNothing() throws NumberParseException {

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
    void testValidPhoneNumber_WhenPhoneNumberIsInvalidNumber_ShouldThrowInvalidPhoneNumberException() throws NumberParseException {

        String invalidPhoneNumber = "+551199876543200000";

        InvalidPhoneNumberException exception = assertThrows(
                InvalidPhoneNumberException.class,
                () -> PhoneNumberValidator.validatePhoneNumber(invalidPhoneNumber)
        );

        assertNotNull(exception);
        assertEquals(InvalidPhoneNumberException.ERROR.formatErrorMessage(
                String.format(INVALID_PHONE_MESSAGE, invalidPhoneNumber)
        ), exception.getMessage());
    }


    @Test
    void validatePhoneNumber_ShouldThrowException_WhenPhoneNumberDoesNotContainPlusSign() {

        String invalidPhoneNumber = "1234567890";

        NumberParseException exception = assertThrows(
                NumberParseException.class,
                () -> PhoneNumberValidator.validatePhoneNumber(invalidPhoneNumber)
        );

        assertNotNull(exception);
        assertEquals(String.format(INVALID_PHONE_MESSAGE, invalidPhoneNumber), exception.getMessage());
    }

    @Test
    void validatePhoneNumber_ShouldThrowException_WhenPhoneNumberIsInvalidButMissingPlus() {

        String invalidPhoneNumber = "+11998765432";

        InvalidPhoneNumberException exception = assertThrows(
                InvalidPhoneNumberException.class,
                () -> PhoneNumberValidator.validatePhoneNumber(invalidPhoneNumber)
        );

        assertNotNull(exception);
        assertEquals(InvalidPhoneNumberException.ERROR.formatErrorMessage(
                String.format(INVALID_PHONE_MESSAGE, invalidPhoneNumber)
        ), exception.getMessage());
    }


}
