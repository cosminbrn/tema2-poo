package main.tickets.enums;

public enum BusinessValue {
    S,
    M,
    L,
    XL;

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
