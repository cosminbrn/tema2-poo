package main.globals.ticketenums;

import lombok.Getter;

public enum BusinessValue {
    S(1),
    M(3),
    L(6),
    XL(10);

    @Getter
    public final int value;

    BusinessValue(int value) {
        this.value = value;
    }

    /**
     * Converts a string representation to its corresponding BusinessValue enum.
     * @param value the string representation of the BusinessValue
     * @return the corresponding BusinessValue enum constant
     */
    public static BusinessValue fromString(String value) {
        return switch (value) {
            case "S" -> S;
            case "M" -> M;
            case "L" -> L;
            case "XL" -> XL;
            default -> throw new IllegalArgumentException("Unknown BusinessValue: " + value);
        };
    }
}
