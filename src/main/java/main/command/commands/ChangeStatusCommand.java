package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.globals.commandenums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.globals.ticketenums.Status;
import main.tickets.Ticket;
import main.users.User;
import main.globals.userenums.Role;

import static main.globals.ticketenums.ActionType.STATUS_CHANGED;
import static main.globals.ticketenums.Status.CLOSED;
import static main.globals.userenums.Role.DEVELOPER;

/**
 * Command to change the status of a ticket by a developer.
 */
public class ChangeStatusCommand extends Command {
    /**
     * Execute the change status command and append output or errors.
     * @param commandInput parsed command input
     * @param output JSON array to append results to
     */
    @Override
    public void execute(final CommandInput commandInput, final ArrayNode output) {
        if (!validateCommand(commandInput, output, Role.DEVELOPER)) {
            return;
        }

        Ticket ticket = db.getTicketById(commandInput.getTicketID());
        if (!ticket.getAssignedTo().equals(commandInput.getUsername())) {
            String msg = String.format(
                    ErrorMessages.TICKET_NOT_ASSIGNED_TO_DEVELOPER_WITHOUT_THE.getErrorMessage(),
                    ticket.getId(), commandInput.getUsername());
            addErrorOutput(commandInput, output, msg);
            return;
        }

        if (ticket.getStatus() == CLOSED) {
            return;
        }

        Status previous = ticket.getStatus();
        Status updated = ticket.updateStatus(commandInput.getTimestamp());
        ticket.addAction(STATUS_CHANGED, commandInput.getUsername(),
                commandInput.getTimestamp(), previous, updated);
    }
}
