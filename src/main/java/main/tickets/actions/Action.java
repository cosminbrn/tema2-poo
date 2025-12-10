package main.tickets.actions;

import lombok.Getter;
import main.tickets.enums.ActionType;

/**
 * Abstract class representing a generic action performed on a ticket.
 */
@Getter
public abstract class Action {
    private final ActionType actionType;
    private final String by;
    private final String timestamp;

    protected Action(ActionType actionType, String by, String timestamp) {
        this.actionType = actionType;
        this.by = by;
        this.timestamp = timestamp;
    }

    protected Action(Action other) {
        this.actionType = other.actionType;
        this.by = other.by;
        this.timestamp = other.timestamp;
    }

    public abstract Action deepCopy();
}
