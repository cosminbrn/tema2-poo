package main.globals;

/**
 * Enum representing different areas of expertise.
 */
public enum ExpertiseArea {
    FRONTEND,
    BACKEND,
    DEVOPS,
    DESIGN,
    DB;

    /**
     * Get ExpertiseArea by name (case insensitive)
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
