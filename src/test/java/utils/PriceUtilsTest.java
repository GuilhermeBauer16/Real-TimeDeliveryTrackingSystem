package utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.PriceLowerThanZeroException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.PriceUtils;
import constants.TestConstants;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PriceUtilsTest {

    private static final String PRICE_LOWER_THAN_ZERO_MESSAGE = "The price can not be less than 0.";

    @Test
    void testCheckIfPriceIsHigherThanZero_WhenPriceIsLowerThanZero_ShouldThrowPriceLowerThanZeroException() {

        PriceLowerThanZeroException exception = assertThrows(PriceLowerThanZeroException.class,
                () -> PriceUtils.checkIfPriceIsHigherThanZero(TestConstants.PRODUCT_INVALID_PRICE));

        assertNotNull(exception);
        assertEquals(PriceLowerThanZeroException.ERROR.formatErrorMessage(PRICE_LOWER_THAN_ZERO_MESSAGE), exception.getMessage());
    }

    @Test
    void testCheckIfPriceIsHigherThanZero_WhenSuccessful_ShouldDoNothing() {

        assertDoesNotThrow(() -> PriceUtils.checkIfPriceIsHigherThanZero(TestConstants.PRODUCT_PRICE));
    }


}
