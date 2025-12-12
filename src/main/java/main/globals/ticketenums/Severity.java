package main.globals.ticketenums;

import lombok.Getter;

public enum Severity {
    MINOR(1, "minor"),
    MODERATE(2, "moderate"),
    SEVERE(3, "severe");

    @Getter
    private final int value;
    private final String string;

    Severity(final int value, final String string) {
        this.value = value;
        this.string = string;
    }

    public String getString() {
        return string;
    }

    /**
     * Converts a string representation to its corresponding Severity enum.
     * @param string the string representation of the Severity
     * @return the corresponding Severity enum constant
     */
    public static Severity fromString(final String string) {
        return switch (string.toLowerCase()) {
            case "minor" -> MINOR;
            case "moderate" -> MODERATE;
            case "severe" -> SEVERE;
            default -> throw new IllegalArgumentException("Unknown severity level: " + string);
        };
    }
}
