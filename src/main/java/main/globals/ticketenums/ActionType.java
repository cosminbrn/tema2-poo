package main.globals.ticketenums;

import lombok.Getter;

public enum ActionType {
    ASSIGNED("ASSIGNED"),
    DE_ASSIGNED("DE-ASSIGNED"),
    STATUS_CHANGED("STATUS_CHANGED"),
    ADDED_TO_MILESTONE("ADDED_TO_MILESTONE"),
    REMOVED_FROM_DEV("REMOVED_FROM_DEV");

    @Getter
    private final String action;

    ActionType(String action) {
        this.action = action;
    }
}
