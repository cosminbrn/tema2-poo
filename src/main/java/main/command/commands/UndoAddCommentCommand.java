package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.database.Database;
import main.fileio.CommandInput;
import main.tickets.Ticket;
import main.users.User;

import java.util.List;

import static main.command.enums.ErrorMessages.COMMENTS_NOT_ALLOWED_ON_ANONYMOUS_TICKETS;

public class UndoAddCommentCommand extends Command {
    @Override
    public void execute(CommandInput commandInput, ArrayNode output) {
        Database db = Database.getInstance();
        Ticket ticket = db.getTicketById(commandInput.getTicketID());
        if (ticket == null) {
            return;
        }

        if (ticket.getReportedBy().isEmpty()) {
            addErrorOutput(commandInput, output, COMMENTS_NOT_ALLOWED_ON_ANONYMOUS_TICKETS.getErrorMessage());
            return;
        }

        List<Ticket.Comment> commentsByUser = ticket.getCommentsByUser(commandInput.getUsername());
        if (commentsByUser.isEmpty()) {
            return;
        }


        User user = db.getUserByUsername(commandInput.getUsername());
        ticket.removeLastCommentByUser(user.getUsername());
        user.removeLastCommentedTicket();
    }
}
