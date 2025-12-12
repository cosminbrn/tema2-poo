package main.tickets.actions;

import lombok.Getter;
import main.globals.ticketenums.Status;

import static main.globals.ticketenums.ActionType.STATUS_CHANGED;

/**
 * Class representing a status change action on a ticket.
 */
@Getter
public class StatusChangeAction extends Action {
    private final Status from;
    private final Status to;

    public StatusChangeAction(final String by,
                              final String timestamp,
                              final Status from,
                              final Status to) {
        super(STATUS_CHANGED, by, timestamp);
        this.from = from;
        this.to = to;
    }

    protected StatusChangeAction(final StatusChangeAction action) {
        super(action);
        this.from = action.from;
        this.to = action.to;
    }

    /**
     * Create a deep copy of this status change action.
     *
     * @param action the original status change action
     * @return a new S with the same data
     */
    @Override
    public Action deepCopy() {
        return new StatusChangeAction(this);
    }
}
