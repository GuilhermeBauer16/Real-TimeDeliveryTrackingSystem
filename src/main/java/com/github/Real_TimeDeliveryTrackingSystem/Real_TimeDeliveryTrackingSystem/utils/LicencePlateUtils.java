package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidLicensePlateException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.LicensePlateNotFoundException;

public class LicencePlateUtils {

    private LicencePlateUtils() {
    }

    private static final String LICENSE_PLATE_NOT_FOUND = "The license plate cannot be null or empty!";
    private static final String INVALID_LICENSE_PLATE = "The license plate %s is invalid. Please verify if the license plate matches " +
            "with these patters XXX-0000 | XXX0XX0";
    private static final String OLD_LICENSE_PLATE_PATTERN = "^[A-Z]{3}-\\d{4}$";
    private static final String NEW_LICENSE_PLATE_PATTERN = "^[a-zA-Z]{3}\\d[a-zA-Z]{2}\\d$";


    public static String validateLicencePlate(String licencePlate) {

        licencePlate = licencePlate.toUpperCase();

        if (licencePlate.isBlank() || licencePlate.isEmpty()) {

            throw new LicensePlateNotFoundException(LICENSE_PLATE_NOT_FOUND);

        }
        if (licencePlate.matches("^[A-Z]{3}\\d{4}$")) {

            licencePlate = licencePlate.substring(0, 3) + "-" + licencePlate.substring(3);
        }

        if (licencePlate.matches(OLD_LICENSE_PLATE_PATTERN) || licencePlate.matches(NEW_LICENSE_PLATE_PATTERN)) {

            return licencePlate;
        }

        throw new InvalidLicensePlateException(String.format(INVALID_LICENSE_PLATE, licencePlate));

    }

}
