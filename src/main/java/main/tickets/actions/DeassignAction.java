package main.tickets.actions;

import static main.globals.ticketenums.ActionType.DE_ASSIGNED;

/**
 * Class representing a de-assignment action on a ticket.
 */
public class DeassignAction extends Action {
    public DeassignAction(final String by, final String timestamp) {
        super(DE_ASSIGNED, by, timestamp);
    }

    protected DeassignAction(final DeassignAction deassignAction) {
        super(deassignAction);
    }

    /**
     * Create a deep copy of this de-assignment action.
     * @return a new one with the same data
     */
    @Override
    public Action deepCopy() {
        return new DeassignAction(this);
    }
}
