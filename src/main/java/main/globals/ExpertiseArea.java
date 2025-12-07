package main.globals;

public enum ExpertiseArea {
    FRONTEND,
    BACKEND,
    DEVOPS,
    DESIGN,
    DB;

    public static ExpertiseArea getExpertiseAreaByName(final String name) {
        for (ExpertiseArea area : ExpertiseArea.values()) {
            if (area.name().equalsIgnoreCase(name)) {
                return area;
            }
        }
        return null;
    }
}
