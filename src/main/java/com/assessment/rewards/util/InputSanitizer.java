package com.assessment.rewards.util;

public final class InputSanitizer {

    private InputSanitizer() {
    }

    /**
     * Strips control characters (newlines, tabs, carriage returns, etc.)
     * from user-supplied input before it is written to logs.
     */
    public static String sanitizeForLog(String value) {
        if (value == null) {
            return "null";
        }
        return value.replaceAll("[\\r\\n\\t]", "_");
    }
}
