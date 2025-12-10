package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.globals.commandenums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.globals.userenums.ExpertiseArea;
import main.milestones.Milestone;
import main.tickets.Ticket;
import main.globals.ticketenums.BusinessPriority;
import main.users.Developer;
import main.globals.userenums.Role;

import static main.globals.ticketenums.ActionType.ASSIGNED;
import static main.globals.ticketenums.ActionType.STATUS_CHANGED;
import static main.globals.ticketenums.BusinessPriority.HIGH;
import static main.globals.ticketenums.BusinessPriority.LOW;
import static main.globals.ticketenums.BusinessPriority.MEDIUM;
import static main.globals.ticketenums.Status.IN_PROGRESS;
import static main.globals.ticketenums.Status.OPEN;
import static main.globals.userenums.Role.DEVELOPER;


/**
 * Command that assigns a ticket to a developer when all preconditions are met.
 */
public class AssignTicketCommand extends Command {
    /**
     * Execute assign ticket command and append output or errors.
     * @param commandInput parsed command input
     * @param output JSON array to append results to
     */
    @Override
    public void execute(final CommandInput commandInput, final ArrayNode output) {
        Database db = Database.getInstance();

        if (db.getUserByUsername(commandInput.getUsername()) == null) {
            String msg =
                    String.format(ErrorMessages.USER_NOT_FOUND.getErrorMessage(),
                            commandInput.getUsername());
            addErrorOutput(commandInput, output, msg);
            return;
        }

        Role role = db.getUserByUsername(commandInput.getUsername()).getRole();
        if (role != DEVELOPER) {
            String msg = String.format(ErrorMessages.REQUIRED_ROLE_DEVELOPER.getErrorMessage(),
                    role.getRoleName().toUpperCase());
            addErrorOutput(commandInput, output, msg);
            return;
        }

        Ticket ticket = db.getTicketById(commandInput.getTicketID());
        Developer developer = (Developer) db.getUserByUsername(commandInput.getUsername());
        ExpertiseArea expertiseArea = ticket.getExpertiseArea();
        if (!developer.hasExpertise(expertiseArea)) {
            String requiredExpertiseArea = getRequiredExpertiseArea(expertiseArea);
            String msg = String.format(ErrorMessages.DEVELOPER_LACKS_EXPERTISE.getErrorMessage(),
                    developer.getUsername(), ticket.getId(),
                    requiredExpertiseArea, developer.getExpertiseArea());
            addErrorOutput(commandInput, output, msg);
            return;
        }

        BusinessPriority bp = ticket.getBusinessPriority();

        if (!developer.hasAccess(bp)) {
            String requiredSeniorityLevel = getRequiredSeniorityLevel(bp);
            String msg = String.format(ErrorMessages.DEVELOPER_LACKS_SENIORITY.getErrorMessage(),
                    developer.getUsername(), ticket.getId(), requiredSeniorityLevel,
                    developer.getSeniority());
            addErrorOutput(commandInput, output, msg);
            return;
        }

        if (ticket.getStatus() != OPEN) {
            addErrorOutput(commandInput, output, ErrorMessages.TICKET_NOT_OPEN.getErrorMessage());
            return;
        }

        String milestoneName = ticket.getAssignedMilestone();
        if (!developer.isAssignedToMilestone(milestoneName)) {
            String msg =
                    String.format(
                            ErrorMessages.DEVELOPER_NOT_ASSIGNED_TO_MILESTONE.getErrorMessage(),
                    developer.getUsername(), milestoneName);
            addErrorOutput(commandInput, output, msg);
            return;
        }

        Milestone milestone = db.getMilestoneByName(milestoneName);
        if (milestone.isBlocked()) {
            String msg = String.format(ErrorMessages.MILESTONE_BLOCKED.getErrorMessage(),
                    ticket.getId(), milestoneName);
            addErrorOutput(commandInput, output, msg);
            return;
        }

        ticket.setAssignedTo(developer.getUsername());
        ticket.setAssignedAt(commandInput.getTimestamp());
        ticket.setStatus(IN_PROGRESS);

        ticket.addAction(ASSIGNED, commandInput.getUsername(), commandInput.getTimestamp());
        ticket.addAction(STATUS_CHANGED, commandInput.getUsername(), commandInput.getTimestamp(),
                OPEN, IN_PROGRESS);

        var devList = milestone.getRepartition().get(developer.getUsername());
        devList.add(ticket.getId());
        developer.addAssignedTicket(ticket);
    }

    private static String getRequiredSeniorityLevel(final BusinessPriority bp) {
        String requiredSeniorityLevel;
        if (bp == LOW || bp == MEDIUM) {
            requiredSeniorityLevel = "JUNIOR, MID, SENIOR";
        } else if (bp == HIGH) {
            requiredSeniorityLevel = "MID, SENIOR";
        } else {
            requiredSeniorityLevel = "SENIOR";
        }
        return requiredSeniorityLevel;
    }

    private static String getRequiredExpertiseArea(final ExpertiseArea expertiseArea) {
        String requiredExpertiseArea;
        if (expertiseArea == ExpertiseArea.DB) {
            requiredExpertiseArea = "BACKEND, DB, FULLSTACK";
        } else if (expertiseArea == ExpertiseArea.FRONTEND) {
            requiredExpertiseArea = "FULLSTACK, FRONTEND";
        } else if (expertiseArea == ExpertiseArea.BACKEND) {
            requiredExpertiseArea = "FULLSTACK, BACKEND";
        } else if (expertiseArea == ExpertiseArea.DESIGN) {
            requiredExpertiseArea = "FULLSTACK, FRONTEND, DESIGN";
        } else {
            requiredExpertiseArea = "DEVOPS";
        }
        return requiredExpertiseArea;
    }
}
