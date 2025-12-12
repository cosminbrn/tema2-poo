package main.command.commands.undo;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.globals.commandenums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.globals.ticketenums.Status;
import main.milestones.Milestone;
import main.tickets.Ticket;
import main.users.Developer;
import main.globals.userenums.Role;

import static main.globals.commandenums.ErrorMessages.TICKET_NOT_IN_PROGRESS;
import static main.globals.ticketenums.ActionType.DE_ASSIGNED;
import static main.globals.ticketenums.Status.IN_PROGRESS;
import static main.globals.ticketenums.Status.OPEN;
import static main.globals.userenums.Role.DEVELOPER;

/**
 * Command to undo the assignment of a ticket from a developer.
 */
public class UndoAssignTicketCommand extends Command {

    /**
     * Execute undo assign ticket command and update ticket and developer state.
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

        Role role = db.getUserByUsername(commandInput.getUsername()).getRole();
        if (role != DEVELOPER) {
            addErrorOutput(commandInput, output,
                    String.format(ErrorMessages.REQUIRED_ROLE_DEVELOPER.getErrorMessage(),
                            role.getRoleName().toUpperCase()));
            return;
        }

        Developer developer = (Developer) db.getUserByUsername(commandInput.getUsername());
        Status status = db.getTicketById(commandInput.getTicketID()).getStatus();

        if (status != IN_PROGRESS) {
            addErrorOutput(commandInput, output,
                    TICKET_NOT_IN_PROGRESS.getErrorMessage());
            return;
        }
        Ticket ticket = developer.getAssignedTicketById(commandInput.getTicketID());
        ticket.setAssignedTo("");
        ticket.setAssignedAt("");
        ticket.setStatus(OPEN);

        ticket.addAction(DE_ASSIGNED, commandInput.getUsername(), commandInput.getTimestamp());

        Milestone milestone = db.getMilestoneByName(ticket.getAssignedMilestone());
        if (milestone != null) {
            milestone.getRepartition().get(developer.getUsername()).removeIf(ticketId ->
                    ticketId == ticket.getId());
        }

        Ticket ticketCopy = ticket.deepCopy();

        developer.addPreviouslyAssignedTicket(ticketCopy);
        developer.removeTicketFromAssigned(ticket);
    }
}
