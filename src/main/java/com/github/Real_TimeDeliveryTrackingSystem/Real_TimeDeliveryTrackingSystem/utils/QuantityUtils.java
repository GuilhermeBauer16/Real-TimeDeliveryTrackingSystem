package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.QuantityLowerThanOneException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.QuantityRequiredHigherThanAvailableQuantityException;

/**
 * Utility class for handling quantity-related validations.
 * Provides methods to validate whether quantities meet certain conditions.
 */

public class QuantityUtils {


    private static final String QUANTITY_LOWER_THAN_ONE_MESSAGE = "The quantity can not be less than 1.";
    private static final String QUANTITY_REQUIRED_HIGHER_THAN_AVAILABLE_QUANTITY_MESSAGE = "the quantity required can not be greater than the available quantity!";

    /**
     * Checks if the given quantity is greater than or equal to one.
     *
     * @param quantity the quantity to check
     * @throws QuantityLowerThanOneException if the quantity is less than one
     */

    public static void checkIfQuantityIsHigherThanOne(Integer quantity) {

        if (quantity < 1) {
            throw new QuantityLowerThanOneException(QUANTITY_LOWER_THAN_ONE_MESSAGE);
        }
    }

    /**
     * Verifies if the required quantity is less than or equal to the available quantity.
     *
     * @param requiredQuantity  the quantity required
     * @param availableQuantity the available quantity
     * @throws QuantityRequiredHigherThanAvailableQuantityException if the required quantity exceeds the available quantity
     */
    public static void verifyIfQuantityRequiredIsHigherThanTheAvailable(Integer requiredQuantity, Integer availableQuantity) {

        if (requiredQuantity > availableQuantity) {
            throw new QuantityRequiredHigherThanAvailableQuantityException(QUANTITY_REQUIRED_HIGHER_THAN_AVAILABLE_QUANTITY_MESSAGE);
        }
    }
}
