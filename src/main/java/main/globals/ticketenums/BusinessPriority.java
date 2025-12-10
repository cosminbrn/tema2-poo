package main.globals.ticketenums;

import lombok.Getter;

public enum BusinessPriority {
    LOW("LOW", 1),
    MEDIUM("MEDIUM", 2),
    HIGH("HIGH", 3),
    CRITICAL("CRITICAL", 4);

    @Getter
    private final String label;
    @Getter
    private final int value;

    BusinessPriority(String label, int value) {
        this.label = label;
        this.value = value;
    }

    /**
     * Converts a string to its corresponding BusinessPriority enum constant.
     * @param value the string representation of the priority
     * @return the corresponding BusinessPriority enum constant
     */
    public static BusinessPriority fromString(String value) {
        for (BusinessPriority priority : BusinessPriority.values()) {
            if (priority.name().equalsIgnoreCase(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
