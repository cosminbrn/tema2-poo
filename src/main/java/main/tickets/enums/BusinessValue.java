package main.tickets.enums;

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
