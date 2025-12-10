package main.tickets.enums;

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

    public static Severity fromString(String str) {
        return switch (str.toLowerCase()) {
            case "minor" -> MINOR;
            case "moderate" -> MODERATE;
            case "severe" -> SEVERE;
            default -> throw new IllegalArgumentException("Unknown severity level: " + str);
        };
    }
}
