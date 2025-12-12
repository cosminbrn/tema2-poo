package main.globals.ticketenums;

/**
 * Enum representing customer demand levels with numeric values.
 */
public enum CustomerDemand {
    LOW(1),
    MEDIUM(3),
    HIGH(6),
    VERY_HIGH(10);

    private final int value;

    CustomerDemand(final int value) {
        this.value = value;
    }

    /**
     * Returns the numeric value for this demand level.
     * @return demand value
     */
    public int getValue() {
        return value;
    }

    /**
     * Converts a string representation to its corresponding CustomerDemand enum.
     * @param value the string representation of the CustomerDemand
     * @return the corresponding CustomerDemand enum constant
     */
    public static CustomerDemand fromString(final String value) {
        return switch (value.toLowerCase()) {
            case "low" -> LOW;
            case "medium" -> MEDIUM;
            case "high" -> HIGH;
            case "very_high" -> VERY_HIGH;
            default -> throw
                    new IllegalArgumentException("Unknown CustomerDemand value: " + value);
        };
    }
}
