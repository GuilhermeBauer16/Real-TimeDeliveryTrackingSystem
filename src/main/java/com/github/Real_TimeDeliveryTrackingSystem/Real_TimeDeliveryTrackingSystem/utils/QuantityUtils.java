package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.QuantityLowerThanOneException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.QuantityRequiredHigherThanAvailableQuantityException;

public class QuantityUtils {


    private static final String QUANTITY_LOWER_THAN_ONE_MESSAGE = "The quantity can not be less than 1.";
    private static final String QUANTITY_REQUIRED_HIGHER_THAN_AVAILABLE_QUANTITY_MESSAGE = "the quantity required can not be greater than the available quantity!";

    public static void checkIfQuantityIsHigherThanOne(Integer quantity){

        if(quantity < 1){
            throw new QuantityLowerThanOneException(QUANTITY_LOWER_THAN_ONE_MESSAGE);
        }
    }

    public static void verifyIfQuantityRequiredIsHigherThanTheAvailable(Integer requiredQuantity, Integer availableQuantity){

        if(requiredQuantity > availableQuantity){
            throw new QuantityRequiredHigherThanAvailableQuantityException(QUANTITY_REQUIRED_HIGHER_THAN_AVAILABLE_QUANTITY_MESSAGE);
        }
    }
}
