package utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidDriverLicenseException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.DriverLicenseValidatorUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DriverLicenseValidatorUtilsTest {

    private static final String INVALID_DRIVER_LICENSE_MESSAGE = "The driver license is invalid. " +
            "Please verify the fields and try again!";

    private final String DRIVER_LICENSE = "35034151605";
    private final String INVALID_DRIVER_LICENSE = "12345";
    private final String INVALID_DRIVER_LICENSE_WITH_SAME_DIGITS = "11111111111";
    private final String INVALID_DRIVER_LICENSE_CHECKSUM = "12345678901";
    private final String INVALID_DRIVER_LICENSE_BY_FIRST_NUMBER_CHECKSUM = "98765432109";


    @Test
    void testValidateDriverLicense_WhenDriverLicenseIsValid_DoNothing() {

        assertDoesNotThrow(() -> DriverLicenseValidatorUtils.validateDriverLicense(DRIVER_LICENSE));

    }

    @Test
    void testValidateDriverLicense_WhenDriverLicenseLengthIsLessThan11Digits_ShouldThrowInvalidDriverLicenseException() {

        InvalidDriverLicenseException exception = assertThrows(InvalidDriverLicenseException.class,
                () -> DriverLicenseValidatorUtils.validateDriverLicense(INVALID_DRIVER_LICENSE));

        assertNotNull(exception);
        assertEquals(InvalidDriverLicenseException.ERROR.formatErrorMessage(INVALID_DRIVER_LICENSE_MESSAGE), exception.getMessage());


    }


    @Test
    void testValidateDriverLicense_WhenDriverLicenseHaveAllSameDigits_ShouldThrowInvalidDriverLicenseException() {

        InvalidDriverLicenseException exception = assertThrows(InvalidDriverLicenseException.class,
                () -> DriverLicenseValidatorUtils.validateDriverLicense(INVALID_DRIVER_LICENSE_WITH_SAME_DIGITS));

        assertNotNull(exception);
        assertEquals(InvalidDriverLicenseException.ERROR.formatErrorMessage(INVALID_DRIVER_LICENSE_MESSAGE), exception.getMessage());
    }

    @Test
    void testValidateDriverLicense_WhenDriverLicenseIsInvalidByTheChecksum_ShouldThrowInvalidDriverLicenseException() {

        InvalidDriverLicenseException exception = assertThrows(InvalidDriverLicenseException.class,
                () -> DriverLicenseValidatorUtils.validateDriverLicense(INVALID_DRIVER_LICENSE_CHECKSUM));

        assertNotNull(exception);
        assertEquals(InvalidDriverLicenseException.ERROR.formatErrorMessage(INVALID_DRIVER_LICENSE_MESSAGE), exception.getMessage());
    }


    @Test
    void testValidateDriverLicense_WhenDriverLicenseIsInvalidByTheFirstNumberChecksumIsHigherThan10_ShouldThrowInvalidDriverLicenseException() {

        InvalidDriverLicenseException exception = assertThrows(InvalidDriverLicenseException.class,
                () -> DriverLicenseValidatorUtils.validateDriverLicense(INVALID_DRIVER_LICENSE_BY_FIRST_NUMBER_CHECKSUM));

        assertNotNull(exception);
        assertEquals(InvalidDriverLicenseException.ERROR.formatErrorMessage(INVALID_DRIVER_LICENSE_MESSAGE), exception.getMessage());
    }
}
