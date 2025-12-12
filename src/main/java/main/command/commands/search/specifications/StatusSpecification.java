package main.command.commands.search.specifications;

import main.globals.Specification;
import main.tickets.Ticket;
import main.globals.ticketenums.Status;

public final class StatusSpecification implements Specification<Ticket> {
    private final Status requiredStatus;

    public StatusSpecification(final Status requiredStatus) {
        this.requiredStatus = requiredStatus;
    }

    /**
     * Checks if the ticket's status matches the required status.
     * @param ticket to check
     * @return true if the ticket's status matches, false otherwise
     */
    @Override
    public boolean isSatisfiedBy(final Ticket ticket) {
        return ticket.getStatus() == this.requiredStatus;
    }
}
