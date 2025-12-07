package main.tickets.enums;

public enum Frequency {
    RARE,
    OCCASIONAL,
    FREQUENT,
    ALWAYS;

    public static Frequency fromString(String value) {
        for (Frequency frequency : Frequency.values()) {
            if (frequency.name().equalsIgnoreCase(value)) {
                return frequency;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
