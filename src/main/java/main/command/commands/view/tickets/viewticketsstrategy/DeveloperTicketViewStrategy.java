package main.command.commands.view.tickets.viewticketsstrategy;

import main.database.Database;
import main.milestones.Milestone;
import main.tickets.Ticket;
import main.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

import static main.globals.ticketenums.Status.OPEN;

/**
 * Strategy to filter tickets for developers (open tickets from their milestones).
 */
public class DeveloperTicketViewStrategy implements TicketFilteringStrategy {
    /**
     * Get tickets visible to the developer user.
     * @param user requesting user
     * @return list of tickets
     */
    @Override
    public List<Ticket> getTickets(final User user) {
        List<Ticket> result = new ArrayList<>();

        Database db = Database.getInstance();
        List<Milestone> developerMilestones =
                Database.getInstance().getMilestonesByDeveloper(user.getUsername());
        for (Milestone milestone : developerMilestones) {
            for (Ticket ticket : db.getTicketsByIds(milestone.getOpenTickets())) {
                if (ticket.getStatus() == OPEN) {
                    result.add(ticket);
                }
            }
        }
        result.sort(
                Comparator.comparing(Ticket::getCreatedAt).reversed()
                        .thenComparingInt(Ticket::getId)
        );
        return result;
    }
}
