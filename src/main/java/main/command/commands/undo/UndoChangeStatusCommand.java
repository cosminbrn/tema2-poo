package main.command.commands.undo;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.globals.commandenums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.tickets.Ticket;
import main.users.User;
import main.globals.userenums.Role;

import static main.globals.commandenums.ErrorMessages.REQUIRED_ROLE_DEVELOPER;
import static main.globals.commandenums.ErrorMessages.TICKET_NOT_ASSIGNED_TO_DEVELOPER_WITHOUT_THE;
import static main.globals.ticketenums.ActionType.STATUS_CHANGED;

import static main.globals.ticketenums.Status.IN_PROGRESS;
import static main.globals.userenums.Role.DEVELOPER;

/**
 * Command to undo the last status change on a ticket by a developer.
 */
public class UndoChangeStatusCommand extends Command {
    /**
     * Execute undo change status command and update ticket actions.
     * @param commandInput parsed command input
     * @param output JSON array to append results to
     */
    @Override
    public void execute(final CommandInput commandInput, final ArrayNode output) {
        Database db = Database.getInstance();

        if (db.getUserByUsername(commandInput.getUsername()) == null) {
            addErrorOutput(commandInput, output,
                    String.format(ErrorMessages.USER_NOT_FOUND.getErrorMessage(),
                            commandInput.getUsername()));
            return;
        }

        User user = db.getUserByUsername(commandInput.getUsername());
        Role role = user.getRole();
        if (role != DEVELOPER) {
            addErrorOutput(commandInput, output,
                    String.format(REQUIRED_ROLE_DEVELOPER.getErrorMessage(),
                            role.getRoleName()));
            return;
        }

        Ticket ticket = db.getTicketById(commandInput.getTicketID());
        if (!ticket.getAssignedTo().equals(commandInput.getUsername())) {
            addErrorOutput(commandInput, output,
                    String.format(TICKET_NOT_ASSIGNED_TO_DEVELOPER_WITHOUT_THE.getErrorMessage(),
                            ticket.getId(), commandInput.getUsername()));
            return;
        }

        if (ticket.getStatus() == IN_PROGRESS) {
            return;
        }
        ticket.addAction(STATUS_CHANGED, commandInput.getUsername(),
                commandInput.getTimestamp(), ticket.getStatus(),
                ticket.undoStatus());
    }
}
