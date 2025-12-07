package main.tickets.enums;

public enum Severity {
    MINOR,
    MODERATE,
    SEVERE;

    public static Severity fromString(String str) {
        return switch (str.toLowerCase()) {
            case "minor" -> MINOR;
            case "moderate" -> MODERATE;
            case "severe" -> SEVERE;
            default -> throw new IllegalArgumentException("Unknown severity level: " + str);
        };
    }
}
