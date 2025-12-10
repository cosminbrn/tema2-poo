package main.globals;

import lombok.Getter;

/**
 * Enum representing the stability status.
 */
public enum Stability {
    STABLE("STABLE"),
    UNSTABLE("UNSTABLE"),
    PARTIALLY_STABLE("PARTIALLY_STABLE");

    @Getter
    public final String name;

    Stability(String name) {
        this.name = name;
    }
}
