package main.tickets.enums;

import lombok.Getter;

public enum BusinessPriority {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    CRITICAL("CRITICAL");

    @Getter
    private final String label;

    BusinessPriority(String label) {
        this.label = label;
    }

    public static BusinessPriority fromString(String value) {
        for (BusinessPriority priority : BusinessPriority.values()) {
            if (priority.name().equalsIgnoreCase(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
