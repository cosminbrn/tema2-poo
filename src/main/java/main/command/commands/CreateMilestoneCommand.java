package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.command.enums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.milestones.Milestone;
import main.tickets.Ticket;
import main.tickets.enums.ActionType;
import main.users.Manager;
import main.users.enums.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static main.tickets.enums.ActionType.ADDED_TO_MILESTONE;
import static main.users.enums.Role.MANAGER;

public class CreateMilestoneCommand extends Command {

    @Override
    public void execute(CommandInput commandInput, ArrayNode output) {
        Database db = Database.getInstance();

        if (db.getUserByUsername(commandInput.getUsername()) == null) {
            addErrorOutput(commandInput, output, String.format(ErrorMessages.USER_NOT_FOUND.getErrorMessage(), commandInput.getUsername()));
            return;
        }

        Role role = db.getUserByUsername(commandInput.getUsername()).getRole();
        if (role != MANAGER) {
            addErrorOutput(commandInput, output, String.format(ErrorMessages.REQUIRED_ROLE_MANAGER.getErrorMessage(), role.getRoleName().toUpperCase()));
            return;
        }

        List<Ticket> tickets = db.getTicketsByIds(commandInput.getTickets());
        for (Ticket ticket : tickets) {
            if (!ticket.getAssignedMilestone().isEmpty()) {
                addErrorOutput(commandInput, output, String.format(ErrorMessages.TICKET_ALREADY_ASSIGNED_TO_MILESTONE.getErrorMessage(), ticket.getId(), ticket.getAssignedMilestone()));
                return;
            }
        }

        Manager manager = (Manager) db.getUserByUsername(commandInput.getUsername());
        Milestone milestone = new Milestone.Builder()
                .setName(commandInput.getName())
                .setCreatedBy(commandInput.getUsername())
                .setCreatedAt(LocalDate.parse(commandInput.getTimestamp()))
                .setDueDate(LocalDate.parse(commandInput.getDueDate()))
                .setTickets(commandInput.getTickets())
                .setBlockingFor(commandInput.getBlockingFor())
                .setAssignedDevs(commandInput.getAssignedDevs())
                .build();

        manager.addCreatedMilestone(milestone);
        String[] assignedDevs = milestone.getAssignedDevs();
        for (String dev : assignedDevs) {
            milestone.getRepartition().putIfAbsent(dev, new ArrayList<>());
        }

        for (String milestoneName : milestone.getBlockingFor()) {
            Milestone blockedMilestone = db.getMilestoneByName(milestoneName);
            if (blockedMilestone != null) {
                blockedMilestone.blockMilestone();
            }
        }

        for (int ticket : milestone.getTickets()) {
            Ticket t = db.getTicketById(ticket);
            t.setAssignedMilestone(milestone.getName());
            t.addAction(ADDED_TO_MILESTONE, commandInput.getUsername(), commandInput.getTimestamp(), milestone.getName());
        }

        db.addMilestone(milestone);
    }
}
