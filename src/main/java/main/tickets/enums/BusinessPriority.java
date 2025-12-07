package main.tickets.enums;

public enum BusinessPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    public static BusinessPriority fromString(String value) {
        for (BusinessPriority priority : BusinessPriority.values()) {
            if (priority.name().equalsIgnoreCase(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
