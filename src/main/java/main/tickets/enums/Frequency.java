package main.tickets.enums;

import lombok.Getter;

public enum Frequency {
    RARE(1),
    OCCASIONAL(2),
    FREQUENT(3),
    ALWAYS(4);

    @Getter
    private final int value;

    Frequency(int value) {
        this.value = value;
    }

    public static Frequency fromString(String value) {
        for (Frequency frequency : Frequency.values()) {
            if (frequency.name().equalsIgnoreCase(value)) {
                return frequency;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
