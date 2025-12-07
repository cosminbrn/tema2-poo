package main.tickets.enums;

public enum CustomerDemand {
    LOW,
    MEDIUM,
    HIGH,
    VERY_HIGH;

    public static CustomerDemand fromString(String value) {
        return switch (value.toLowerCase()) {
            case "low" -> LOW;
            case "medium" -> MEDIUM;
            case "high" -> HIGH;
            case "very_high" -> VERY_HIGH;
            default -> throw new IllegalArgumentException("Unknown CustomerDemand value: " + value);
        };
    }
}
