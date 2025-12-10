package main.command.commands.search.specifications;

import main.globals.Specification;
import main.tickets.Ticket;
import main.globals.ticketenums.Status;

public class StatusSpecification implements Specification<Ticket> {
    private final Status requiredStatus;

    public StatusSpecification(final Status requiredStatus) {
        this.requiredStatus = requiredStatus;
    }

    @Override
    public boolean isSatisfiedBy(Ticket ticket) {
        return ticket.getStatus() == this.requiredStatus;
    }
}
