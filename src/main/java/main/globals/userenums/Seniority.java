package main.globals.userenums;

import lombok.Getter;

public enum Seniority {
    JUNIOR("JUNIOR"),
    MID("MID"),
    SENIOR("SENIOR");

    @Getter
    private final String name;

    Seniority(final String name) {
        this.name = name;
    }

    /**
     * Gets the Seniority enum constant by its name, ignoring case.
     * @param name the name of the seniority level
     * @return the corresponding Seniority, or null if not found
     */
    public static Seniority getSeniorityByName(final String name) {
        for (Seniority seniority : Seniority.values()) {
            if (seniority.name().equalsIgnoreCase(name)) {
                return seniority;
            }
        }
        return null;
    }
}
