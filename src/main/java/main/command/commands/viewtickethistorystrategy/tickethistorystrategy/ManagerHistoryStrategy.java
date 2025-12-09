package main.command.commands.viewtickethistorystrategy.tickethistorystrategy;

import main.database.Database;
import main.milestones.Milestone;
import main.tickets.Ticket;
import main.users.Manager;
import main.users.User;

import java.util.ArrayList;
import java.util.List;

public class ManagerHistoryStrategy implements HistoryFilteringStrategy {
    @Override
    public List<Ticket> getTickets(User user) {
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
