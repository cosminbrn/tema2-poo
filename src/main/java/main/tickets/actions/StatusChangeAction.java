package main.tickets.actions;

import lombok.Getter;
import main.tickets.enums.Status;

import static main.tickets.enums.ActionType.STATUS_CHANGED;

/**
 * Class representing a status change action on a ticket.
 */
@Getter
public class StatusChangeAction extends Action {
    private final Status from;
    private final Status to;

    public StatusChangeAction(String by, String timestamp, Status from, Status to) {
        super(STATUS_CHANGED, by, timestamp);
        this.from = from;
        this.to = to;
    }
}
