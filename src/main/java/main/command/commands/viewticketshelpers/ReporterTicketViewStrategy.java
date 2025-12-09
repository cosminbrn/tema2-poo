package main.command.commands.viewticketshelpers;

import main.database.Database;
import main.tickets.Ticket;
import main.users.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/**
 * Strategy interface for filtering tickets based on reporter.
 */
public class ReporterTicketViewStrategy implements TicketFilteringStrategy {
    @Override
    public List<Ticket> getTickets(User user) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : Database.getInstance().getTickets()) {
            if (ticket.getReportedBy().equals(user.getUsername())) {
                result.add(ticket);
            }
        }
        result.sort(Comparator.comparing(Ticket::getCreatedAt, Comparator.reverseOrder()).thenComparingInt(Ticket::getId));
        return result;
    }
}
