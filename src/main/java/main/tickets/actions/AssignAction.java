package main.tickets.actions;

import static main.tickets.enums.ActionType.ASSIGNED;

/**
 * Class representing an assignment action on a ticket.
 */
public class AssignAction extends Action {
    public AssignAction(String by, String timestamp) {
        super(ASSIGNED, by, timestamp);
    }

    protected AssignAction(AssignAction action) {
        super(action);
    }

    @Override
    public Action deepCopy() {
        return new AssignAction(this);
    }
}
