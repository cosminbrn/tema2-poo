package main.command.commands.view.history.viewtickethistorystrategy.tickethistorystrategy;

import main.database.Database;
import main.milestones.Milestone;
import main.tickets.Ticket;
import main.users.Manager;
import main.users.User;

import java.util.ArrayList;
import java.util.List;

/**
 * History strategy for managers: aggregates tickets from manager-created milestones.
 */
public class ManagerHistoryStrategy implements HistoryFilteringStrategy {
    /**
     * Get tickets relevant to the manager.
     * @param user manager user
     * @return list of tickets
     */
    @Override
    public List<Ticket> getTickets(final User user) {
        Database db = Database.getInstance();
        Manager manager = (Manager) user;
        List<Ticket> result = new ArrayList<>();
        for (Milestone milestone : manager.getCreatedMilestones()) {
            result.addAll(db.getTicketsByIds(milestone.getOpenTickets()));
            result.addAll(db.getTicketsByIds(milestone.getClosedTickets()));
        }
        return result;
    }
}
