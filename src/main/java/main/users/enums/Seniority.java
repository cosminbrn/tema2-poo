package main.users.enums;

import lombok.Getter;

public enum Seniority {
    JUNIOR("JUNIOR"),
    MID("MID"),
    SENIOR("SENIOR");

    @Getter
    private final String name;

    Seniority(String name) {
        this.name = name;
    }

    public static Seniority getSeniorityByName(String name) {
        for (Seniority seniority : Seniority.values()) {
            if (seniority.name().equalsIgnoreCase(name)) {
                return seniority;
            }
        }
        return null;
    }
}
