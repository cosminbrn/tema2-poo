package main.command.commands.view;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.command.Command;
import main.database.Database;
import main.fileio.CommandInput;
import main.globals.commandenums.ErrorMessages;
import main.globals.userenums.Role;
import main.tickets.Ticket;
import main.users.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static main.App.MAPPER;
import static main.globals.commandenums.CommandType.VIEW_ASSIGNED_TICKETS;

/**
 * Command to view tickets assigned to the current user.
 */
public class ViewAssignedTicketsCommand extends Command {
    /**
     * Execute view assigned tickets command and append assigned tickets array.
     * @param input parsed command input
     * @param output JSON array to append results to
     */
    @Override
    public void execute(final CommandInput input, final ArrayNode output) {
        Database db = Database.getInstance();
        List<Ticket> tickets = new ArrayList<>();

        for (Ticket ticket : db.getTickets()) {
            if (ticket.getAssignedTo().equals(input.getUsername())) {
                tickets.add(ticket);
            }
        }

        User user = db.getUserByUsername(input.getUsername());
        if (user.getRole() != Role.DEVELOPER) {
            addErrorOutput(input, output, String.format(ErrorMessages.REQUIRED_ROLE_DEVELOPER.getErrorMessage(), user.getRole().getRoleName().toUpperCase()));
            return;
        }


        tickets.sort(Comparator.comparing(Ticket::getBusinessPriority, Comparator.reverseOrder())
                .thenComparing(Ticket::getCreatedAt)
                .thenComparingInt(Ticket::getId));

        ArrayNode assignedTicketsArray = MAPPER.createArrayNode();
        for (Ticket ticket : tickets) {
            ObjectNode ticketNode = MAPPER.createObjectNode();

            ticketNode.put("id", ticket.getId());
            ticketNode.put("type", ticket.getType().getTypeName());
            ticketNode.put("title", ticket.getTitle());
            ticketNode.put("businessPriority", ticket.getBusinessPriority().getLabel());
            ticketNode.put("status", ticket.getStatus().getStatusName());
            ticketNode.put("createdAt", ticket.getCreatedAt());
            ticketNode.put("assignedAt", ticket.getAssignedAt());
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

            assignedTicketsArray.add(ticketNode);
        }
        addOutput(input, output, assignedTicketsArray);
    }

    /**
     * Helper to append assigned tickets to the output array.
     * @param input parsed command input
     * @param output JSON array to append results to
     * @param assignedTicketsArray array of assigned tickets to include
     */
    public void addOutput(final CommandInput input,
                          final ArrayNode output,
                          final ArrayNode assignedTicketsArray) {
        node.put("command", VIEW_ASSIGNED_TICKETS.getName());
        node.put("username", input.getUsername());
        node.put("timestamp", input.getTimestamp());
        node.set("assignedTickets", assignedTicketsArray);
        output.add(node);
    }
}
