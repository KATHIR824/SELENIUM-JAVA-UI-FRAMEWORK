package com.company.framework.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for formatting dates and timestamps.
 */
public final class DateUtils {

    private DateUtils() {
        // Prevent instantiation
    }

    /**
     * Gets the current date/time formatted according to the given pattern.
     *
     * @param format pattern format string (e.g. "yyyy-MM-dd HH:mm:ss")
     * @return formatted date/time string
     */
    public static String getCurrentDateTime(String format) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }
}
