package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.command.enums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.globals.ExpertiseArea;
import main.milestones.Milestone;
import main.tickets.Ticket;
import main.tickets.enums.BusinessPriority;
import main.tickets.enums.Status;
import main.users.Developer;
import main.users.enums.Role;
import main.users.enums.Seniority;

import static main.command.enums.ErrorMessages.*;
import static main.globals.ExpertiseArea.*;
import static main.tickets.enums.BusinessPriority.*;
import static main.tickets.enums.Status.IN_PROGRESS;
import static main.tickets.enums.Status.OPEN;
import static main.users.enums.Role.DEVELOPER;
import static main.users.enums.Role.MANAGER;

public class AssignTicketCommand extends Command {
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

        Ticket ticket = db.getTicketById(commandInput.getTicketID());
        Developer developer = (Developer) db.getUserByUsername(commandInput.getUsername());
        ExpertiseArea expertiseArea = ticket.getExpertiseArea();
        if (!developer.hasExpertise(expertiseArea)) {
            String requiredExpertiseArea = getRequiredExpertiseArea(expertiseArea);
            addErrorOutput(commandInput, output, String.format(DEVELOPER_LACKS_EXPERTISE.getErrorMessage(), developer.getUsername(), ticket.getId(), requiredExpertiseArea, developer.getExpertiseArea()));
            return;
        }

        BusinessPriority bp = ticket.getBusinessPriority();

        if (!developer.hasAccess(bp)) {
            String requiredSeniorityLevel = getRequiredSeniorityLevel(bp);
            addErrorOutput(commandInput, output, String.format(DEVELOPER_LACKS_SENIORITY.getErrorMessage(), developer.getUsername(), ticket.getId(), requiredSeniorityLevel, developer.getSeniority()));
            return;
        }

        if (ticket.getStatus() != OPEN) {
            addErrorOutput(commandInput, output, TICKET_NOT_OPEN.getErrorMessage());
            return;
        }

        String milestoneName = ticket.getAssignedMilestone();
        if (!developer.isAssignedToMilestone(milestoneName)) {
            addErrorOutput(commandInput, output, String.format(DEVELOPER_NOT_ASSIGNED_TO_MILESTONE.getErrorMessage(), developer.getUsername(), milestoneName));
            return;
        }

        if (db.getMilestoneByName(milestoneName).isBlocked()) {
            addErrorOutput(commandInput, output, String.format(MILESTONE_BLOCKED.getErrorMessage(), ticket.getId(), milestoneName));
            return;
        }
        ticket.setAssignedTo(developer.getUsername());
        ticket.setAssignedAt(commandInput.getTimestamp());
        ticket.setStatus(IN_PROGRESS);

        Milestone milestone = db.getMilestoneByName(milestoneName);
        milestone.getRepartition().get(developer.getUsername()).add(ticket.getId());

        developer.addAssignedTicket(ticket);
    }

    private static String getRequiredSeniorityLevel(BusinessPriority bp) {
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

    private static String getRequiredExpertiseArea(ExpertiseArea expertiseArea) {
        String requiredExpertiseArea;
        if (expertiseArea == DB) {
            requiredExpertiseArea = "BACKEND, DB, FULLSTACK";
        } else if (expertiseArea == FRONTEND) {
            requiredExpertiseArea = "FULLSTACK, FRONTEND";
        } else if (expertiseArea == BACKEND) {
            requiredExpertiseArea = "FULLSTACK, BACKEND";
        } else if (expertiseArea == DESIGN) {
            requiredExpertiseArea = "FULLSTACK, FRONTEND, DESIGN";
        } else {
            requiredExpertiseArea = "DEVOPS";
        }
        return requiredExpertiseArea;
    }
}
