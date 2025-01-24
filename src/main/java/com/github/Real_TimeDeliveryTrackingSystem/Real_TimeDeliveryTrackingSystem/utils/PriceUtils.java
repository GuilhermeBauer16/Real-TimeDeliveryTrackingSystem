package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.PriceLowerThanZeroException;

/**
 * Utility class for handling price-related validations.
 * Provides methods to validate whether prices meet certain conditions.
 */
public class PriceUtils {


    private static final String PRICE_LOWER_THAN_ZERO_MESSAGE = "The price can not be less than 0.";

    /**
     * Checks if the given price is greater than zero.
     *
     * @param price the price to check
     * @throws PriceLowerThanZeroException if the price is less than or equal to zero
     */
    public static void checkIfPriceIsHigherThanZero(Double price) {

        if (price <= 0D) {
            throw new PriceLowerThanZeroException(PRICE_LOWER_THAN_ZERO_MESSAGE);
        }
    }

}
