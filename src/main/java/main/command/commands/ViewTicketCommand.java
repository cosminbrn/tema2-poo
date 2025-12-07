package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.command.commands.viewticketshelpers.ManagerViewStrategy;
import main.command.commands.viewticketshelpers.ReporterViewStrategy;
import main.command.commands.viewticketshelpers.TicketFilteringStrategy;
import main.database.Database;
import main.fileio.CommandInput;
import main.tickets.Ticket;
import main.users.User;

import java.util.Comparator;
import java.util.List;

import static main.App.MAPPER;
import static main.command.enums.CommandType.VIEW_TICKETS;

public class ViewTicketCommand extends Command {
    List<Ticket> tickets;

    @Override
    public void execute(CommandInput input, ArrayNode output) {
        Database db = Database.getInstance();
        User user = db.getUserByUsername(input.getUsername());

        TicketFilteringStrategy strategy = switch (user.getRole()) {
            case REPORTER -> new ReporterViewStrategy();
            case DEVELOPER -> null;
            case MANAGER -> new ManagerViewStrategy();
        };

        assert strategy != null;
        tickets = strategy.getTickets(user);
        tickets.sort(Comparator.comparing(Ticket::getCreatedAt).thenComparingInt(Ticket::getId));
        addOutput(input, output);
    }

    public void addOutput(CommandInput input, ArrayNode output) {
        node.put("command", VIEW_TICKETS.getName());
        node.put("username", input.getUsername());
        node.put("timestamp", input.getTimestamp());
        node.set("tickets", MAPPER.valueToTree(tickets));
        output.add(node);
    }
}
