package utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidEmailPatternException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.EmailValidatorUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailValidatorUtilsTest {

    private static final String INVALID_EMAIL_PATTERN_EXCEPTION_MESSAGE = "This email pattern is invalid, please verify if matches with that example" +
            " exampleEmail@Domain.com";

    private static final String EMAIL_NOT_BE_NULL_OR_EMPTY_MESSAGE = "The email can not be null or empty!";

    private static final String VALID_EMAIL = "example@domain.com";

    @Test
    void testValidateEmailPattern_WhenEmailPatternIsCorrect_DoNothing() {

        assertDoesNotThrow(() -> EmailValidatorUtils.verifyEmailPattern(VALID_EMAIL));
    }

    @Test
    void testValidateEmailPattern_WhenEmailIsNull_ShouldThrowInvalidEmailPatternException() {

        InvalidEmailPatternException exception = assertThrows(InvalidEmailPatternException.class, () -> EmailValidatorUtils.verifyEmailPattern(null));
        assertNotNull(exception);
        assertEquals(exception.getMessage(), InvalidEmailPatternException.ERROR.formatErrorMessage(EMAIL_NOT_BE_NULL_OR_EMPTY_MESSAGE));


    }

    @Test
    void testValidateEmailPattern_WhenEmailIsEmpty_ShouldThrowInvalidEmailPatternException() {

        InvalidEmailPatternException exception = assertThrows(InvalidEmailPatternException.class, () -> EmailValidatorUtils.verifyEmailPattern("   "));
        assertNotNull(exception);
        assertEquals(exception.getMessage(), InvalidEmailPatternException.ERROR.formatErrorMessage(EMAIL_NOT_BE_NULL_OR_EMPTY_MESSAGE));


    }

    @Test
    void testValidateEmailPattern_WhenAtIsMissingInEmail_ShouldThrowInvalidEmailPatternException() {
        String emailWithoutAt = "exampledomain.com";
        InvalidEmailPatternException exception = assertThrows(InvalidEmailPatternException.class, () -> EmailValidatorUtils.verifyEmailPattern(emailWithoutAt));
        assertNotNull(exception);
        assertEquals(exception.getMessage(), InvalidEmailPatternException.ERROR.formatErrorMessage(INVALID_EMAIL_PATTERN_EXCEPTION_MESSAGE));


    }

    @Test
    void testValidateEmailPattern_WhenTheDomainIsMissing_ShouldThrowInvalidEmailPatternException() {
        String emailWithoutDomain = "example@";
        InvalidEmailPatternException exception = assertThrows(InvalidEmailPatternException.class, () -> EmailValidatorUtils.verifyEmailPattern(emailWithoutDomain));
        assertNotNull(exception);
        assertEquals(exception.getMessage(), InvalidEmailPatternException.ERROR.formatErrorMessage(INVALID_EMAIL_PATTERN_EXCEPTION_MESSAGE));


    }


}
