package main.command.commands.view.history.viewtickethistorystrategy.tickethistorystrategy;

import main.tickets.Ticket;
import main.users.User;

import java.util.List;

/**
 * Strategy interface for filtering ticket history based on user roles.
 */
public interface HistoryFilteringStrategy {
    /**
     * Gets the list of tickets based on the user's role.
     * @param user The user whose ticket history is to be retrieved.
     * @return List of tickets relevant to the user.
     */
    List<Ticket> getTickets(User user);
}
