package main.tickets.actions;

import lombok.Getter;
import main.globals.ticketenums.ActionType;

/**
 * Base class for all ticket actions, storing common metadata
 * such as type, actor, and timestamp.
 */
@Getter
public abstract class Action {
    private final ActionType actionType;
    private final String by;
    private final String timestamp;

    protected Action(final ActionType actionType,
                     final String by,
                     final String timestamp) {
        this.actionType = actionType;
        this.by = by;
        this.timestamp = timestamp;
    }

    protected Action(final Action other) {
        this.actionType = other.actionType;
        this.by = other.by;
        this.timestamp = other.timestamp;
    }

    /**
     * Returns a deep copy of this action instance.
     * @return deep-copied action
     */
    public abstract Action deepCopy();
}
