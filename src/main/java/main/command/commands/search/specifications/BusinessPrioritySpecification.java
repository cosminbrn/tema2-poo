package main.command.commands.search.specifications;

import main.globals.Specification;
import main.globals.ticketenums.BusinessPriority;
import main.tickets.Ticket;

/**
 * Specification for filtering tickets by priority.
 */
public class BusinessPrioritySpecification implements Specification<Ticket> {
    private final BusinessPriority requiredPriority;

    public BusinessPrioritySpecification(final BusinessPriority requiredPriority) {
        this.requiredPriority = requiredPriority;
    }

    /**
     * Checks if the ticket's priority matches the required priority.
     * @param ticket the ticket to check
     * @return true if the ticket's priority matches the required priority, false otherwise
     */
    @Override
    public boolean isSatisfiedBy(final Ticket ticket) {
        return ticket.getBusinessPriority() == requiredPriority;
    }
}
