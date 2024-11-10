package utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidLicensePlateException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.LicensePlateNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.LicencePlateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class LicensePlaceUtilsTest {

    private static final String LICENSE_PLATE_NOT_FOUND_MESSAGE = "The license plate cannot be null or empty!";
    private static final String INVALID_LICENSE_PLATE_MESSAGE = "The license plate %s is invalid. Please verify if the license plate matches " +
            "with these patters XXX-0000 | XXX0XX0";

    private static final String NEW_LICENSE_PLATE = "AQE1F34";
    private static final String OLD_LICENSE_PLATE = "IOE-1223";
    private static final String OLD_LICENSE_PLATE_WITHOUT_HYPHEN = "IOE1223";
    private static final String INVALID_LICENSE_PLATE = "IOEEE-1223";

    private static final String OLD_LICENSE_PLATE_PATTERN = "^[A-Z]{3}-\\d{4}$";
    private static final String NEW_LICENSE_PLATE_PATTERN = "^[A-Z]{3}\\d[A-Z]\\d{2}$";



    @Test
    void testValidateLicensePlate_WhenTheLicensePlateIsNewPattern_ShouldReturnValidLicensePlate() {

        String validatedLicencePlate = LicencePlateUtils.validateLicencePlate(NEW_LICENSE_PLATE);
        assertNotNull(validatedLicencePlate);
        assertTrue(validatedLicencePlate.matches(NEW_LICENSE_PLATE_PATTERN));
    }

    @Test
    void testValidateLicensePlate_WhenTheLicensePlateIsOldPattern_ShouldReturnValidLicensePlate() {

        String validatedLicencePlate = LicencePlateUtils.validateLicencePlate(OLD_LICENSE_PLATE);
        assertNotNull(validatedLicencePlate);
        assertTrue(validatedLicencePlate.matches(OLD_LICENSE_PLATE_PATTERN));
    }

    @Test
    void testValidateLicensePlate_WhenTheLicensePlateIsOldPatternWithoutHyphen_ShouldReturnValidLicensePlate() {

        String validatedLicencePlate = LicencePlateUtils.validateLicencePlate(OLD_LICENSE_PLATE_WITHOUT_HYPHEN);
        assertNotNull(validatedLicencePlate);
        assertTrue(validatedLicencePlate.matches(OLD_LICENSE_PLATE_PATTERN));
    }


    @Test
    void testValidateLicensePlate_WhenTheLicensePlateIsBlank_ShouldThrowLicensePlateNotFoundException() {

        LicensePlateNotFoundException exception = assertThrows(LicensePlateNotFoundException.class, () ->
            LicencePlateUtils.validateLicencePlate("   "));

        assertNotNull(exception);
        assertEquals(exception.getMessage(), LicensePlateNotFoundException.ERROR.formatErrorMessage(LICENSE_PLATE_NOT_FOUND_MESSAGE));
    }


    @Test
    void testValidateLicensePlate_WhenTheLicensePlateIsInvalid_ShouldThrowInvalidLicensePlateException() {

        InvalidLicensePlateException exception = assertThrows(InvalidLicensePlateException.class, () ->
            LicencePlateUtils.validateLicencePlate(INVALID_LICENSE_PLATE));

        assertNotNull(exception);
        assertEquals(exception.getMessage(), InvalidLicensePlateException.ERROR
                .formatErrorMessage(String.format(INVALID_LICENSE_PLATE_MESSAGE, INVALID_LICENSE_PLATE)));
    }


}
