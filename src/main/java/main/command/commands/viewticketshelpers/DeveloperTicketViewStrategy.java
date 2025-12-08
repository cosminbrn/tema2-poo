package main.command.commands.viewticketshelpers;

import main.database.Database;
import main.milestones.Milestone;
import main.tickets.Ticket;
import main.users.User;

import java.util.ArrayList;
import java.util.List;

public class DeveloperTicketViewStrategy implements TicketFilteringStrategy{
    @Override
    public List<Ticket> getTickets(User user) {
        List<Ticket> result = new ArrayList<>();

        Database db = Database.getInstance();
        List<Milestone> developerMilestones = Database.getInstance().getMilestonesByDeveloper(user.getUsername());
        for (Milestone milestone : developerMilestones) {
           result.addAll(db.getTicketsByIds(milestone.getOpenTickets()));
        }

        return result;
    }
}
