package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.database.Database;
import main.fileio.CommandInput;
import main.tickets.Ticket;
import main.users.User;
import main.users.enums.Role;

import static main.command.enums.ErrorMessages.*;
import static main.users.enums.Role.DEVELOPER;
import static main.users.enums.Role.REPORTER;

/**
 * Command that handles adding comments to tickets.
 */
public class AddCommentCommand extends Command {

    /**
     * Execute add comment command and append output or errors.
     * @param commandInput parsed command input
     * @param output JSON array to append results to
     */
    @Override
    public void execute(CommandInput commandInput, ArrayNode output) {
        Database db = Database.getInstance();

        int ticketID = commandInput.getTicketID();
        Ticket ticket = db.getTicketById(ticketID);
        if (ticket == null) {
            return;
        }

        if (ticket.getReportedBy().isEmpty()) {
            addErrorOutput(commandInput, output, COMMENTS_NOT_ALLOWED_ON_ANONYMOUS_TICKETS.getErrorMessage());
            return;
        }

        Role role = db.getUserByUsername(commandInput.getUsername()).getRole();
        if (role == REPORTER && ticket.getStatus().toString().equals("CLOSED")) {
            addErrorOutput(commandInput, output, REPORTER_TICKET_IS_CLOSED.getErrorMessage());
            return;
        }

        if (commandInput.getComment().length() < 10) {
            addErrorOutput(commandInput, output, NOT_10_CHARACTERS_LONG.getErrorMessage());
            return;
        }

        if (role == DEVELOPER && !ticket.getAssignedTo().equals(commandInput.getUsername())) {
            addErrorOutput(commandInput, output, String.format(TICKET_NOT_ASSIGNED_TO_DEVELOPER.getErrorMessage(), commandInput.getTicketID(), commandInput.getUsername()));
            return;
        }

        if (role == REPORTER && !ticket.getReportedBy().equals(commandInput.getUsername())) {
            addErrorOutput(commandInput, output, String.format(TICKET_NOT_REPORTED_BY_REPORTER.getErrorMessage(), commandInput.getUsername(), ticket.getId()));
            return;
        }
        User user = db.getUserByUsername(commandInput.getUsername());
        ticket.addComment(commandInput.getUsername(), commandInput.getComment(), commandInput.getTimestamp());
        user.addCommentedTicket(ticket);
    }
}
