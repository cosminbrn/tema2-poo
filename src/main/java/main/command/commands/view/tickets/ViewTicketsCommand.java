package main.command.commands.view.tickets;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.command.Command;
import main.command.commands.view.tickets.viewticketsstrategy.DeveloperTicketViewStrategy;
import main.command.commands.view.tickets.viewticketsstrategy.ManagerTicketViewStrategy;
import main.command.commands.view.tickets.viewticketsstrategy.ReporterTicketViewStrategy;
import main.command.commands.view.tickets.viewticketsstrategy.TicketFilteringStrategy;
import main.globals.commandenums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.tickets.Ticket;
import main.users.User;

import java.util.Comparator;
import java.util.List;

import static main.App.MAPPER;
import static main.globals.commandenums.CommandType.VIEW_TICKETS;

/**
 * Command to view tickets for the current user according to role.
 */
public class ViewTicketsCommand extends Command {
    Database db = Database.getInstance();

    /**
     * Execute view tickets command and append the resulting tickets array.
     * @param input  parsed command input
     * @param output JSON array to append results to
     */
    @Override
    public void execute(CommandInput input, ArrayNode output) {

        User user = db.getUserByUsername(input.getUsername());

        if (db.getUserByUsername(input.getUsername()) == null) {
            addErrorOutput(input, output,
                    String.format(ErrorMessages.USER_NOT_FOUND.getErrorMessage(),
                            input.getUsername()));
            return;
        }

        TicketFilteringStrategy strategy = switch (user.getRole()) {
            case REPORTER -> new ReporterTicketViewStrategy();
            case DEVELOPER -> new DeveloperTicketViewStrategy();
            case MANAGER -> new ManagerTicketViewStrategy();
        };
        List<Ticket> tickets = strategy.getTickets(user);
        tickets.sort(Comparator.comparing(Ticket::getCreatedAt)
                .thenComparingInt(Ticket::getId));
        ArrayNode ticketsArray = MAPPER.createArrayNode();
        for (Ticket ticket : tickets) {
            ObjectNode ticketNode = MAPPER.createObjectNode();

            ticketNode.put("id", ticket.getId());
            ticketNode.put("type", ticket.getType().getTypeName());
            ticketNode.put("title", ticket.getTitle());
            ticketNode.put("businessPriority", ticket
                    .getBusinessPriority().getLabel());
            ticketNode.put("status", ticket.getStatus()
                    .getStatusName());
            ticketNode.put("createdAt", ticket.getCreatedAt());
            ticketNode.put("assignedAt", ticket.getAssignedAt());
            ticketNode.put("solvedAt", ticket.getSolvedAt());
            ticketNode.put("assignedTo", ticket.getAssignedTo());
            ticketNode.put("reportedBy", ticket.getReportedBy());

            ArrayNode commentsArray = MAPPER.createArrayNode();
            for (Ticket.Comment comment : ticket.getComments()) {
                ObjectNode commentNode = MAPPER.createObjectNode();
                commentNode.put("author", comment.author());
                commentNode.put("content", comment.comment());
                commentNode.put("createdAt", comment.timestamp());
                commentsArray.add(commentNode);
            }
            ticketNode.set("comments", commentsArray);

            ticketsArray.add(ticketNode);
        }
        addOutput(input, output, ticketsArray);
    }

    /**
     * Helper to append ticket list result to the output array.
     *
     * @param input   parsed command input
     * @param output  JSON array to append results to
     * @param tickets ticket array to include in the response
     */
    public void addOutput(final CommandInput input,
                          final ArrayNode output,
                          final ArrayNode tickets) {
        node.put("command", VIEW_TICKETS.getName());
        node.put("username", input.getUsername());
        node.put("timestamp", input.getTimestamp());
        node.set("tickets", tickets);
        output.add(node);
    }
}
