package com.quicktracker;

import java.util.Scanner;

/*

Class: InputValidator
Purpose:
 - Validates user input safely so the program never crashes.
 - Reused in both CLI and GUI contexts for numeric validation.
*/

public class InputValidator {

    /** Safely get an integer value from a text field or console */
    public static int parseInt(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("⚠ Invalid number format (integer required).");
        }
    }

    /** Safely get a double value from a text field or console */
    public static double parseDouble(String text) {
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("⚠ Invalid number format (decimal required).");
        }
    }

    /** Validate that a text field is not blank */
    public static void requireNonEmpty(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("⚠ Field '" + fieldName + "' cannot be empty.");
        }
    }
}
