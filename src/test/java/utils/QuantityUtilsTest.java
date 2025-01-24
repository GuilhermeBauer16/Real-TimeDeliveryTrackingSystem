package utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.QuantityLowerThanOneException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.QuantityRequiredHigherThanAvailableQuantityException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.QuantityUtils;
import constants.TestConstants;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class QuantityUtilsTest {

    private static final String QUANTITY_LOWER_THAN_ONE_MESSAGE = "The quantity can not be less than 1.";
    private static final String QUANTITY_REQUIRED_HIGHER_THAN_AVAILABLE_QUANTITY_MESSAGE = "the quantity required can not be greater than the available quantity!";


    @Test
    void testCheckIfQuantityIsHigherThanOne_WhenQuantityIsLowerThanOne_ShouldThrowQuantityLowerThanOneException() {

        QuantityLowerThanOneException exception = assertThrows(QuantityLowerThanOneException.class,
                () -> QuantityUtils.checkIfQuantityIsHigherThanOne(TestConstants.PRODUCT_INVALID_QUANTITY));

        assertNotNull(exception);
        assertEquals(QuantityLowerThanOneException.ERROR.formatErrorMessage(QUANTITY_LOWER_THAN_ONE_MESSAGE), exception.getMessage());
    }

    @Test
    void testCheckIfQuantityIsHigherThanOne_WhenQuantityIsHigherThanOne_ShouldDoNothing() {

        assertDoesNotThrow(() -> QuantityUtils.checkIfQuantityIsHigherThanOne(TestConstants.PRODUCT_QUANTITY));
    }

    @Test
    void testVerifyIfQuantityRequiredIsHigherThanTheAvailable_WhenQuantityRequiredIsHigherThanAvailableQuantity_ShouldThrowQuantityRequiredHigherThanAvailableQuantityException() {

        QuantityRequiredHigherThanAvailableQuantityException exception = assertThrows(QuantityRequiredHigherThanAvailableQuantityException.class,
                () -> QuantityUtils.verifyIfQuantityRequiredIsHigherThanTheAvailable(TestConstants.PRODUCT_SECOND_QUANTITY,TestConstants.PRODUCT_QUANTITY));

        assertNotNull(exception);
        assertEquals(QuantityRequiredHigherThanAvailableQuantityException.
                ERROR.formatErrorMessage(QUANTITY_REQUIRED_HIGHER_THAN_AVAILABLE_QUANTITY_MESSAGE), exception.getMessage());
    }

    @Test
    void testVerifyIfQuantityRequiredIsHigherThanTheAvailable_WhenSuccessful_ShouldDoNothing() {

        assertDoesNotThrow(() -> QuantityUtils.verifyIfQuantityRequiredIsHigherThanTheAvailable(TestConstants.PRODUCT_QUANTITY,TestConstants.PRODUCT_SECOND_QUANTITY));
    }

}
