package main.command.commands.viewtickethistorystrategy.tickethistorystrategy;

import main.tickets.Ticket;
import main.users.Developer;
import main.users.User;

import java.util.ArrayList;
import java.util.List;

public class DeveloperHistoryStrategy implements HistoryFilteringStrategy {
    @Override
    public List<Ticket> getTickets(User user) {
        Developer developer = (Developer) user;
        List<Ticket> result = new ArrayList<>();
        result.addAll(developer.getAssignedTickets());
        result.addAll(developer.getPreviouslyAssignedTickets());
        return result;
    }
}
