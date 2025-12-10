package main.tickets.actions;

import static main.globals.ticketenums.ActionType.ASSIGNED;

/**
 * Class representing an assignment action on a ticket.
 */
public class AssignAction extends Action {
    public AssignAction(final String by, final String timestamp) {
        super(ASSIGNED, by, timestamp);
    }

    protected AssignAction(final AssignAction action) {
        super(action);
    }

    @Override
    public Action deepCopy() {
        return new AssignAction(this);
    }
}
