package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.globals.commandenums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.milestones.Milestone;
import main.tickets.Ticket;
import main.users.User;
import main.globals.userenums.Role;

import java.time.LocalDate;

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
        Database db = Database.getInstance();

        if (db.getUserByUsername(commandInput.getUsername()) == null) {
            String msg = String.format(ErrorMessages.USER_NOT_FOUND.getErrorMessage(),
                    commandInput.getUsername());
            addErrorOutput(commandInput, output, msg);
            return;
        }

        User user = db.getUserByUsername(commandInput.getUsername());
        Role role = user.getRole();
        if (role != DEVELOPER) {
            String msg = String.format(ErrorMessages.REQUIRED_ROLE_DEVELOPER.getErrorMessage(),
                    role.getRoleName());
            addErrorOutput(commandInput, output, msg);
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

        var previous = ticket.getStatus();
        var updated = ticket.updateStatus(commandInput.getTimestamp());
        ticket.addAction(STATUS_CHANGED, commandInput.getUsername(),
                commandInput.getTimestamp(), previous, updated);

        if (ticket.getStatus() == CLOSED) {
            Milestone ticketMilestone = db.getMilestoneByName(ticket.getAssignedMilestone());
            ticketMilestone.closeTicket(ticket);
            ticketMilestone.updateMilestone(LocalDate.parse(commandInput.getTimestamp()));
        }
    }
}
