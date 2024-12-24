package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidDriverLicenseException;

public class DriverLicenseValidatorUtils {


    private static final String INVALID_DRIVER_LICENSE_MESSAGE = "The driver license is invalid. " +
            "Please verify the fields and try again!";

    private static boolean isValidDriverLicense(String driverLicense) {
        char char1 = driverLicense.charAt(0);

        if (driverLicense.replaceAll("\\D+", "").length() != 11
                || String.format("%0" + 11 + "d", 0).replace('0', char1).equals(driverLicense)) {
            return false;
        }

        long v = 0, j = 9;

        for (int i = 0; i < 9; ++i, --j) {
            v += ((driverLicense.charAt(i) - 48) * j);
        }

        long dsc = 0, vl1 = v % 11;

        if (vl1 >= 10) {
            vl1 = 0;
            dsc = 2;
        }

        v = 0;
        j = 1;

        for (int i = 0; i < 9; ++i, ++j) {
            v += ((driverLicense.charAt(i) - 48) * j);
        }

        long x = v % 11;
        long vl2 = (x >= 10) ? 0 : x - dsc;

        return (String.valueOf(vl1) + String.valueOf(vl2)).equals(driverLicense.substring(driverLicense.length() - 2));

    }

    public static void validateDriverLicense(String cnh) {
        if (!isValidDriverLicense(cnh)) {
            throw new InvalidDriverLicenseException(INVALID_DRIVER_LICENSE_MESSAGE);
        }
    }

}
