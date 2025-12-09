package main.tickets.actions;

import static main.tickets.enums.ActionType.DE_ASSIGNED;

/**
 * Class representing a de-assignment action on a ticket.
 */
public class DeassignAction extends Action {
    public DeassignAction(String by, String timestamp) {
        super(DE_ASSIGNED, by, timestamp);
    }
}
