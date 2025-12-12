package main.globals.ticketenums;

public enum Frequency {
    RARE(1),
    OCCASIONAL(2),
    FREQUENT(3),
    ALWAYS(4);

    private final int value;

    Frequency(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * Converts a string representation to its corresponding Frequency enum.
     * @param value the string representation of the Frequency
     * @return the corresponding Frequency enum constant
     */
    public static Frequency fromString(final String value) {
        for (Frequency frequency : Frequency.values()) {
            if (frequency.name().equalsIgnoreCase(value)) {
                return frequency;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
