package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.command.Command;
import main.command.commands.viewticketshelpers.DeveloperTicketViewStrategy;
import main.command.commands.viewticketshelpers.ManagerTicketViewStrategy;
import main.command.commands.viewticketshelpers.ReporterTicketViewStrategy;
import main.command.commands.viewticketshelpers.TicketFilteringStrategy;
import main.database.Database;
import main.fileio.CommandInput;
import main.tickets.Ticket;
import main.users.User;

import java.util.Comparator;
import java.util.List;

import static main.App.MAPPER;
import static main.command.enums.CommandType.VIEW_TICKETS;

public class ViewTicketsCommand extends Command {
    Database db = Database.getInstance();

    @Override
    public void execute(CommandInput input, ArrayNode output) {

        User user = db.getUserByUsername(input.getUsername());

        TicketFilteringStrategy strategy = switch (user.getRole()) {
            case REPORTER -> new ReporterTicketViewStrategy();
            case DEVELOPER -> new DeveloperTicketViewStrategy();
            case MANAGER -> new ManagerTicketViewStrategy();
        };
        List<Ticket> tickets = strategy.getTickets(user);
        tickets.sort(Comparator.comparing(Ticket::getCreatedAt).thenComparingInt(Ticket::getId));
        ArrayNode ticketsArray = MAPPER.createArrayNode();
        for (Ticket ticket : tickets) {
            ObjectNode ticketNode = MAPPER.createObjectNode();

            ticketNode.put("id", ticket.getId());
            ticketNode.put("type", ticket.getType().getTypeName());
            ticketNode.put("title", ticket.getTitle());
            ticketNode.put("businessPriority", ticket.getBusinessPriority().getLabel());
            ticketNode.put("status", ticket.getStatus().getStatusName());
            ticketNode.put("createdAt", ticket.getCreatedAt());
            ticketNode.put("assignedAt", ticket.getAssignedAt());
            ticketNode.put("solvedAt", ticket.getSolvedAt());
            ticketNode.put("assignedTo", ticket.getAssignedTo());
            ticketNode.put("reportedBy", ticket.getReportedBy());
            ticketNode.set("comments", MAPPER.valueToTree(ticket.getComments()));
            ticketsArray.add(ticketNode);
        }
        addOutput(input, output, ticketsArray);
    }

    public void addOutput(CommandInput input, ArrayNode output, ArrayNode tickets) {
        node.put("command", VIEW_TICKETS.getName());
        node.put("username", input.getUsername());
        node.put("timestamp", input.getTimestamp());
        node.set("tickets", tickets);
        output.add(node);
    }
}
