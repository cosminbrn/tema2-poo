package main.command.commands.view.history.viewtickethistorystrategy.tickethistorystrategy;

import main.tickets.Ticket;
import main.users.Developer;
import main.users.User;

import java.util.ArrayList;
import java.util.List;

/**
 * History strategy for developers: includes current and previously assigned tickets.
 */
public class DeveloperHistoryStrategy implements HistoryFilteringStrategy {
    /**
     * Get tickets relevant to the developer.
     * @param user developer user
     * @return list of tickets
     */
    @Override
    public List<Ticket> getTickets(final User user) {
        Developer developer = (Developer) user;
        List<Ticket> result = new ArrayList<>();
        result.addAll(developer.getAssignedTickets());
        result.addAll(developer.getPreviouslyAssignedTickets());
        return result;
    }
}
