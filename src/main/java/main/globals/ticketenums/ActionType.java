package main.globals.ticketenums;

public enum ActionType {
    ASSIGNED("ASSIGNED"),
    DE_ASSIGNED("DE-ASSIGNED"),
    STATUS_CHANGED("STATUS_CHANGED"),
    ADDED_TO_MILESTONE("ADDED_TO_MILESTONE"),
    REMOVED_FROM_DEV("REMOVED_FROM_DEV");

    private final String action;

    ActionType(final String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
