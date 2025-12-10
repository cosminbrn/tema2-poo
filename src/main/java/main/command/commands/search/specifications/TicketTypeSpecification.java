package main.command.commands.search.specifications;

import main.globals.Specification;
import main.globals.ticketenums.TicketType;
import main.tickets.Ticket;

/**
 * Specification for filtering tickets by type.
 */
public class TicketTypeSpecification implements Specification<Ticket> {
    private final TicketType requiredTicketType;

    public TicketTypeSpecification(final TicketType ticketType) {
        this.requiredTicketType = ticketType;
    }

    /**
     * Checks if the ticket's type matches the required type.
     * @param ticket ticket to check
     * @return true if the ticket's type matches the required type, false otherwise
     */
    @Override
    public boolean isSatisfiedBy(final Ticket ticket) {
        return ticket.getType() == requiredTicketType;
    }
}
