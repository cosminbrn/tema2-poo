package main.command.commands.undo;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.database.Database;
import main.fileio.CommandInput;
import main.globals.userenums.Role;
import main.tickets.Ticket;
import main.users.User;

import java.util.List;

import static main.globals.commandenums.ErrorMessages.COMMENTS_NOT_ALLOWED_ON_ANONYMOUS_TICKETS;

/**
 * Command to undo the last comment added by a user to a ticket.
 */
public class UndoAddCommentCommand extends Command {
    /**
     * Execute undo add comment command and update ticket and user state.
     * @param commandInput parsed command input
     * @param output JSON array to append results to
     */
    @Override
    public void execute(final CommandInput commandInput, final ArrayNode output) {
        if (!validateCommand(commandInput, output, Role.REPORTER, Role.DEVELOPER)) {
            return;
        }

        Ticket ticket = db.getTicketById(commandInput.getTicketID());
        if (ticket == null) {
            return;
        }

        if (ticket.getReportedBy().isEmpty()) {
            addErrorOutput(commandInput, output,
                    COMMENTS_NOT_ALLOWED_ON_ANONYMOUS_TICKETS.getErrorMessage());
            return;
        }

        List<Ticket.Comment> commentsByUser =
                ticket.getCommentsByUser(commandInput.getUsername());
        if (commentsByUser.isEmpty()) {
            return;
        }


        User user = db.getUserByUsername(commandInput.getUsername());
        ticket.removeLastCommentByUser(user.getUsername());
        user.removeLastCommentedTicket();
    }
}
