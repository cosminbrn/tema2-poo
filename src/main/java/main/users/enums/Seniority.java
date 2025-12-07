package main.users.enums;

public enum Seniority {
    JUNIOR,
    MID,
    SENIOR;

    public static Seniority getSeniorityByName(String name) {
        for (Seniority seniority : Seniority.values()) {
            if (seniority.name().equalsIgnoreCase(name)) {
                return seniority;
            }
        }
        return null;
    }
}
