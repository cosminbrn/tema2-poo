package main.command.commands.viewticketsstrategy;

import main.database.Database;
import main.tickets.Ticket;
import main.users.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ManagerTicketViewStrategy implements TicketFilteringStrategy {
    @Override
    public List<Ticket> getTickets(User user) {
        Database db = Database.getInstance();
        List<Ticket> result = new ArrayList<>();
        result = db.getTickets();
        result.sort(Comparator.comparing(Ticket::getCreatedAt, Comparator.reverseOrder()).thenComparingInt(Ticket::getId));
        return result;
    }
}
