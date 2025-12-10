package main.tickets.enums;

import lombok.Getter;

public enum CustomerDemand {
    LOW(1),
    MEDIUM(3),
    HIGH(6),
    VERY_HIGH(10);

    @Getter
    public final int value;

    CustomerDemand(int value) {
        this.value = value;
    }

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
