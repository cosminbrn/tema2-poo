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
                              final Status from, Status to) {
        super(STATUS_CHANGED, by, timestamp);
        this.from = from;
        this.to = to;
    }

    protected StatusChangeAction(StatusChangeAction action) {
        super(action);
        this.from = action.from;
        this.to = action.to;
    }

    @Override
    public Action deepCopy() {
        return new StatusChangeAction(this);
    }
}
