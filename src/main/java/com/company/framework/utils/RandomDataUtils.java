package com.company.framework.utils;

import java.util.UUID;

/**
 * Utility class for generating random mock test data.
 */
public final class RandomDataUtils {

    private RandomDataUtils() {
        // Prevent instantiation
    }

    /**
     * Generates a random alphanumeric string of 8 characters.
     *
     * @return 8-character random string
     */
    public static String getRandomString() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    /**
     * Generates a mock email address with a random prefix.
     *
     * @return email address string (e.g. "user_a1b2c3d4@example.com")
     */
    public static String getRandomEmail() {
        return "user_" + getRandomString() + "@example.com";
    }
}
