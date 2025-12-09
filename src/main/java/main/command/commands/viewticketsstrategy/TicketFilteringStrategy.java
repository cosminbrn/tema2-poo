package main.command.commands.viewticketsstrategy;

import main.tickets.Ticket;
import main.users.User;

import java.util.List;

public interface TicketFilteringStrategy {
    List<Ticket> getTickets(User user);
}
