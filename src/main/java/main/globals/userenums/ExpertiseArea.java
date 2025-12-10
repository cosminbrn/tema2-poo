package main.globals.userenums;

import lombok.Getter;

/**
 * Enum representing different areas of expertise.
 */
public enum ExpertiseArea {
    FRONTEND("FRONTEND"),
    BACKEND("BACKEND"),
    DEVOPS("DEVOPS"),
    DESIGN("DESIGN"),
    DB("DB"),
    FULLSTACK("FULLSTACK");

    @Getter
    private final String name;

    /**
     * Constructor
     * @param name the name of the expertise area
     */
    ExpertiseArea(final String name) {
        this.name = name;
    }

    /**
     * Get ExpertiseArea by name (case-insensitive)
     * @param name the name of the expertise area
     * @return the ExpertiseArea enum value, or null if not found
     */
    public static ExpertiseArea fromString(final String name) {
        for (ExpertiseArea area : ExpertiseArea.values()) {
            if (area.name().equalsIgnoreCase(name)) {
                return area;
            }
        }
        return null;
    }
}
