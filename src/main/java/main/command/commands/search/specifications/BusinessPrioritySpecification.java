package main.command.commands.search.specifications;

import main.globals.Specification;
import main.globals.ticketenums.BusinessPriority;
import main.tickets.Ticket;

public class BusinessPrioritySpecification implements Specification<Ticket> {
    private final BusinessPriority requiredPriority;

    public BusinessPrioritySpecification(final BusinessPriority requiredPriority) {
        this.requiredPriority = requiredPriority;
    }

    @Override
    public boolean isSatisfiedBy(final Ticket ticket) {
        return ticket.getBusinessPriority() == requiredPriority;
    }
}
