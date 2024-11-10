package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidLicensePlateException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.LicensePlateNotFoundException;

public class LicencePlateUtils {

    private LicencePlateUtils() {}


    private static final String LICENSE_PLATE_NOT_FOUND = "The license plate cannot be null or empty!";
    private static final String INVALID_LICENSE_PLATE = "The license plate %s is invalid. Please verify if the license plate matches " +
            "with these patters XXX-0000 | XXX0XX0";
    private static final String OLD_LICENSE_PLATE_PATTERN = "^[A-Z]{3}-\\d{4}$";
    private static final String NEW_LICENSE_PLATE_PATTERN = "^[A-Z]{3}\\d[A-Z]\\d{2}$";


    public static String validateLicencePlate(String licensePlate) {

        licensePlate = licensePlate.toUpperCase();

        if (licensePlate.isBlank()) {

            throw new LicensePlateNotFoundException(LICENSE_PLATE_NOT_FOUND);

        }
        if (licensePlate.matches("^[A-Z]{3}\\d{4}$")) {

            licensePlate = licensePlate.substring(0, 3) + "-" + licensePlate.substring(3);
        }

        if (licensePlate.matches(OLD_LICENSE_PLATE_PATTERN) || licensePlate.matches(NEW_LICENSE_PLATE_PATTERN)) {

            return licensePlate;
        }

        throw new InvalidLicensePlateException(String.format(INVALID_LICENSE_PLATE, licensePlate));

    }

}
