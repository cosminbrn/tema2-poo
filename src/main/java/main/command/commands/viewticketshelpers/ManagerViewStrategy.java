package main.command.commands.viewticketshelpers;

import main.database.Database;
import main.tickets.Ticket;
import main.users.User;

import java.util.ArrayList;
import java.util.List;

public class ManagerViewStrategy implements TicketFilteringStrategy {
    @Override
    public List<Ticket> getTickets(User user) {
        return new ArrayList<>(Database.getInstance().getTickets());
    }
}
