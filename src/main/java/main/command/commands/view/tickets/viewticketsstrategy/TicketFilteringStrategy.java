package main.command.commands.view.tickets.viewticketsstrategy;

import main.tickets.Ticket;
import main.users.User;

import java.util.List;

/**
 * Strategy interface for filtering tickets based on user role.
 */
public interface TicketFilteringStrategy {
    /**
     * Get the list of tickets visible to the provided user.
     * @param user the user requesting tickets
     * @return list of tickets
     */
    List<Ticket> getTickets(User user);
}
