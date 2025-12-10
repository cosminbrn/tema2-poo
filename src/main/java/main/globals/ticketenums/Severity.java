package main.globals.ticketenums;

import lombok.Getter;

public enum Severity {
    MINOR(1),
    MODERATE(2),
    SEVERE(3);

    @Getter
    private final int value;

    Severity(int value) {
        this.value = value;
    }

    /**
     * Converts a string representation to its corresponding Severity enum.
     * @param string the string representation of the Severity
     * @return the corresponding Severity enum constant
     */
    public static Severity fromString(String string) {
        return switch (string.toLowerCase()) {
            case "minor" -> MINOR;
            case "moderate" -> MODERATE;
            case "severe" -> SEVERE;
            default -> throw new IllegalArgumentException("Unknown severity level: " + string);
        };
    }
}
