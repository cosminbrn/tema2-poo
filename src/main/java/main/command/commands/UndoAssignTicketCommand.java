package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.command.enums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.milestones.Milestone;
import main.tickets.Ticket;
import main.users.Developer;
import main.users.enums.Role;

import static main.command.enums.ErrorMessages.TICKET_NOT_IN_PROGRESS;
import static main.tickets.enums.Status.IN_PROGRESS;
import static main.tickets.enums.Status.OPEN;
import static main.users.enums.Role.DEVELOPER;

public class UndoAssignTicketCommand extends Command {

    @Override
    public void execute(CommandInput commandInput, ArrayNode output) {
        Database db = Database.getInstance();

        if (db.getUserByUsername(commandInput.getUsername()) == null) {
            addErrorOutput(commandInput, output, String.format(ErrorMessages.USER_NOT_FOUND.getErrorMessage(), commandInput.getUsername()));
            return;
        }

        Role role = db.getUserByUsername(commandInput.getUsername()).getRole();
        if (role != DEVELOPER) {
            addErrorOutput(commandInput, output, String.format(ErrorMessages.REQUIRED_ROLE_DEVELOPER.getErrorMessage(), role.getRoleName().toUpperCase()));
            return;
        }

        Developer developer = (Developer) db.getUserByUsername(commandInput.getUsername());
        Ticket ticket = developer.getAssignedTicketById(commandInput.getTicketID());

        if (ticket.getStatus() != IN_PROGRESS) {
            addErrorOutput(commandInput, output, TICKET_NOT_IN_PROGRESS.getErrorMessage());
            return;
        }

        ticket.setAssignedTo("");
        ticket.setAssignedAt("");
        ticket.setStatus(OPEN);

        Milestone milestone = db.getMilestoneByName(ticket.getAssignedMilestone());
        if (milestone != null) {
            milestone.getRepartition().get(developer.getUsername()).removeIf(ticketId -> ticketId == ticket.getId());
        }

        developer.removeTicketFromAssigned(ticket);
    }
}
