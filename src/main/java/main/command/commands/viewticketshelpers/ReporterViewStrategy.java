package main.command.commands.viewticketshelpers;

import main.database.Database;
import main.tickets.Ticket;
import main.users.User;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
/**
 * Strategy interface for filtering tickets based on reporter.
 */
public class ReporterViewStrategy implements TicketFilteringStrategy {
    @Override
    public List<Ticket> getTickets(User user) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : Database.getInstance().getTickets()) {
            if (ticket.getReportedBy().equals(user.getUsername())) {
                result.add(ticket);
            }
        }
        return result;
    }
}
