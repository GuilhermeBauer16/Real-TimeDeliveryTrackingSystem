package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils;

import java.security.SecureRandom;


/**
 * Utility class for generating random numeric codes.
 * <p>
 * This class provides a static method to generate secure random numeric codes of a specified length.
 * It uses {@link SecureRandom} to ensure the randomness of the generated codes.
 * </p>
 */
public class CodeGeneratorUtils {

    /**
     * Generates a random numeric code of the specified length.
     * <p>
     * The generated code consists of digits (0-9) and is created using a {@link SecureRandom} instance.
     * </p>
     *
     * @param length the length of the numeric code to generate
     * @return a string containing the generated numeric code
     * @throws IllegalArgumentException if the specified length is negative or zero
     */

    public static String generateCode(int length) {

        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }
}
