package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.globals.commandenums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.tickets.Ticket;
import main.users.User;
import main.globals.userenums.Role;

import static main.globals.userenums.Role.DEVELOPER;
import static main.globals.userenums.Role.REPORTER;

/**
 * Command that handles adding comments to tickets.
 */
public class AddCommentCommand extends Command {

    private static final int MIN_COMMENT_LENGTH = 10;
    /**
     * Execute add comment command and append output or errors.
     * @param commandInput parsed command input
     * @param output JSON array to append results to
     */
    @Override
    public void execute(final CommandInput commandInput, final ArrayNode output) {
        Database db = Database.getInstance();

        int ticketID = commandInput.getTicketID();
        Ticket ticket = db.getTicketById(ticketID);
        if (ticket == null) {
            return;
        }

        if (ticket.getReportedBy().isEmpty()) {
            addErrorOutput(commandInput, output,
                    ErrorMessages.COMMENTS_NOT_ALLOWED_ON_ANONYMOUS_TICKETS.getErrorMessage());
            return;
        }

        Role role = db.getUserByUsername(commandInput.getUsername()).getRole();
        if (role == REPORTER && ticket.getStatus().toString().equals("CLOSED")) {
            addErrorOutput(commandInput, output,
                    ErrorMessages.REPORTER_TICKET_IS_CLOSED.getErrorMessage());
            return;
        }

        if (commandInput.getComment().length() < MIN_COMMENT_LENGTH) {
            addErrorOutput(commandInput,
                    output, ErrorMessages.NOT_10_CHARACTERS_LONG.getErrorMessage());
            return;
        }

        if (role == DEVELOPER && !ticket.getAssignedTo().equals(commandInput.getUsername())) {
            String msg =
                    String.format(ErrorMessages.TICKET_NOT_ASSIGNED_TO_DEVELOPER.getErrorMessage(),
                    commandInput.getTicketID(), commandInput.getUsername());
            addErrorOutput(commandInput, output, msg);
            return;
        }

        if (role == REPORTER && !ticket.getReportedBy().equals(commandInput.getUsername())) {
            String msg =
                    String.format(ErrorMessages.TICKET_NOT_REPORTED_BY_REPORTER.getErrorMessage(),
                    commandInput.getUsername(), ticket.getId());
            addErrorOutput(commandInput, output, msg);
            return;
        }
        User user = db.getUserByUsername(commandInput.getUsername());
        ticket.addComment(commandInput.getUsername(),
                commandInput.getComment(), commandInput.getTimestamp());
        user.addCommentedTicket(ticket);
    }
}
