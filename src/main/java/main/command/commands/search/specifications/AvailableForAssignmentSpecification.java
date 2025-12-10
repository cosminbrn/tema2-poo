package main.command.commands.search.specifications;

import main.globals.Specification;
import main.tickets.Ticket;
import main.globals.ticketenums.Status;
import main.users.Developer;

public class AvailableForAssignmentSpecification implements Specification<Ticket> {
    private final Developer dev;

    public AvailableForAssignmentSpecification(final Developer dev) {
        this.dev = dev;
    }
    @Override
    public boolean isSatisfiedBy(final Ticket ticket) {
        if (ticket.getStatus() != Status.OPEN) {
            return false;
        }

        if (!dev.hasExpertise(ticket.getExpertiseArea())) {
            return false;
        }

        if (!dev.hasAccess(ticket.getBusinessPriority())) {
            return false;
        }

        return dev.isAssignedToMilestone(ticket.getAssignedMilestone());
    }
}
