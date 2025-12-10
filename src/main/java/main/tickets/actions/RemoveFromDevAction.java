package main.tickets.actions;

import lombok.Getter;

import static main.tickets.enums.ActionType.REMOVED_FROM_DEV;

/**
 * Class representing a removal from development action on a ticket.
 */
@Getter
public class RemoveFromDevAction extends Action {
    private final String from;
    public RemoveFromDevAction(String by, String timestamp, String from) {
        super(REMOVED_FROM_DEV, by, timestamp);
        this.from = from;
    }

    protected RemoveFromDevAction(RemoveFromDevAction action) {
        super(action);
        this.from = action.from;
    }

    @Override
    public Action deepCopy() {
        return new RemoveFromDevAction(this);
    }
}
