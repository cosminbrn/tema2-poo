package main.tickets.actions;

import lombok.Getter;

import static main.globals.ticketenums.ActionType.REMOVED_FROM_DEV;

/**
 * Class representing a removal from development action on a ticket.
 */
@Getter
public class RemoveFromDevAction extends Action {
    private final String from;
    public RemoveFromDevAction(final String by,
                               final String timestamp,
                               final String from) {
        super(REMOVED_FROM_DEV, by, timestamp);
        this.from = from;
    }

    protected RemoveFromDevAction(final RemoveFromDevAction action) {
        super(action);
        this.from = action.from;
    }

    @Override
    public Action deepCopy() {
        return new RemoveFromDevAction(this);
    }
}
