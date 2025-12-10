package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.command.enums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.milestones.Milestone;
import main.tickets.Ticket;
import main.users.Developer;
import main.users.Manager;
import main.users.enums.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static main.milestones.enums.MilestoneMessage.MILESTONE_CREATION;
import static main.tickets.enums.ActionType.ADDED_TO_MILESTONE;
import static main.users.enums.Role.MANAGER;

/**
 * Command that creates a milestone and assigns tickets and developers to it.
 */
public class CreateMilestoneCommand extends Command {

    /**
     * Execute the create-milestone command and append any outputs to the provided output array.
     * @param commandInput the parsed command input
     * @param output the array to append results to
     */
    @Override
    public void execute(final CommandInput commandInput, final ArrayNode output) {
        Database db = Database.getInstance();

        if (db.getUserByUsername(commandInput.getUsername()) == null) {
            addErrorOutput(commandInput, output, String.format(
                    ErrorMessages.USER_NOT_FOUND.getErrorMessage(),
                    commandInput.getUsername()));
            return;
        }

        Role role = db.getUserByUsername(commandInput.getUsername()).getRole();
        if (role != MANAGER) {
            addErrorOutput(commandInput, output, String.format(
                    ErrorMessages.REQUIRED_ROLE_MANAGER.getErrorMessage(),
                    role.getRoleName().toUpperCase()));
            return;
        }

        List<Ticket> tickets = db.getTicketsByIds(commandInput.getTickets());
        for (Ticket ticket : tickets) {
            if (!ticket.getAssignedMilestone().isEmpty()) {
                addErrorOutput(commandInput, output, String.format(
                        ErrorMessages.TICKET_ALREADY_ASSIGNED_TO_MILESTONE
                                .getErrorMessage(),
                        ticket.getId(), ticket.getAssignedMilestone()));
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
            Developer developer = (Developer) db.getUserByUsername(dev);
            milestone.addObserver(developer);
        }

        milestone.notifyObservers(String.format(
                MILESTONE_CREATION.getMessage(),
                milestone.getName(), commandInput.getDueDate()));

        for (String milestoneName : milestone.getBlockingFor()) {
            Milestone blockedMilestone = db.getMilestoneByName(milestoneName);
            if (blockedMilestone != null) {
                blockedMilestone.blockMilestone();
            }
        }

        for (int ticket : milestone.getTickets()) {
            Ticket t = db.getTicketById(ticket);
            t.setAssignedMilestone(milestone.getName());
            t.addAction(ADDED_TO_MILESTONE, commandInput.getUsername(),
                    commandInput.getTimestamp(), milestone.getName());
        }

        db.addMilestone(milestone);
    }
}
