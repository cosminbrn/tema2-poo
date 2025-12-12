package main.command.commands.view.tickets.viewticketsstrategy;

import main.database.Database;
import main.tickets.Ticket;
import main.users.User;

import java.util.Comparator;
import java.util.List;

public final class ManagerTicketViewStrategy implements TicketFilteringStrategy {
    @Override
    public List<Ticket> getTickets(final User user) {
        Database db = Database.getInstance();
        List<Ticket> result = db.getTickets();
        result.sort(
                Comparator.comparing(Ticket::getCreatedAt).reversed()
                        .thenComparingInt(Ticket::getId)
        );
        return result;
    }
}
