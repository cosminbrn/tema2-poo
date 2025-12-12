package main.command.commands.search.specifications;

import main.globals.Specification;
import main.tickets.Ticket;
import main.globals.ticketenums.Status;
import main.users.Developer;

public final class AvailableForAssignmentSpecification implements Specification<Ticket> {
    private final Developer dev;

    public AvailableForAssignmentSpecification(final Developer dev) {
        this.dev = dev;
    }

    /**
     * Checks if a ticket is available for assignment to the developer.
     * @param ticket the ticket to check for availability
     * @return true if the ticket is available for assignment to the developer,
     *         false otherwise
     */
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
