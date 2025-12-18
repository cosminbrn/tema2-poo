package main.command.commands.view.milestones;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.command.Command;
import main.command.commands.view.milestones.viewmilestonesstrategy.DeveloperMilestoneViewStrategy;
import main.command.commands.view.milestones.viewmilestonesstrategy.ManagerMilestoneViewStrategy;
import main.command.commands.view.milestones.viewmilestonesstrategy.MilestoneFilteringStrategy;
import main.globals.commandenums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.globals.userenums.Role;
import main.milestones.Milestone;
import main.users.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static main.App.MAPPER;

/**
 * Command to view milestones for the current user.
 */
public class ViewMilestonesCommand extends Command {

    /**
     * Execute view milestones command and append milestones array to output.
     * @param commandInput parsed command input
     * @param output JSON array to append results to
     */
    @Override
    public void execute(final CommandInput commandInput, final ArrayNode output) {
        if (!validateCommand(commandInput, output, Role.MANAGER, Role.DEVELOPER)) {
            return;
        }

        User user = db.getUserByUsername(commandInput.getUsername());


        MilestoneFilteringStrategy strategy = switch (user.getRole()) {
            case DEVELOPER -> new DeveloperMilestoneViewStrategy();
            case MANAGER -> new ManagerMilestoneViewStrategy();
            default -> throw new IllegalStateException("Unexpected value: " + user.getRole());
        };

        List<Milestone> milestones = strategy.getMilestones(user);
        milestones.sort(Comparator.comparing(Milestone::getDueDate)
                .thenComparing(Milestone::getName));
        ArrayNode milestonesArray = MAPPER.createArrayNode();
        for (Milestone milestone : milestones) {
            LocalDate date = LocalDate.parse(commandInput.getTimestamp());
            milestone.updateMilestone(date);
            ObjectNode milestoneNode = MAPPER.createObjectNode();

            milestoneNode.put("name", milestone.getName());
            milestoneNode.set("blockingFor", MAPPER.valueToTree(milestone.getBlockingFor()));
            milestoneNode.put("dueDate", milestone.getDueDate().toString());
            milestoneNode.put("createdAt", milestone.getCreatedAt().toString());
            milestoneNode.set("tickets", MAPPER.valueToTree(milestone.getTickets()));
            milestoneNode.set("assignedDevs", MAPPER.valueToTree(milestone.getAssignedDevs()));
            milestoneNode.put("createdBy", milestone.getCreatedBy());
            milestoneNode.put("status", milestone.getStatus().getState());
            milestoneNode.put("isBlocked", milestone.isBlocked());
            milestoneNode.put("daysUntilDue",
                    milestone.calculateDaysUntilDue(LocalDate.parse(commandInput.getTimestamp())));
            milestoneNode.put("overdueBy",
                    milestone.calculateOverdueBy(LocalDate.parse(commandInput.getTimestamp())));
            milestoneNode.set("openTickets", MAPPER.valueToTree(milestone.getOpenTickets()));
            milestoneNode.set("closedTickets",
                    MAPPER.valueToTree(milestone.getSortedClosedTickets()));
            milestoneNode.put("completionPercentage",
                    milestone.calculateCompletionPercentage());

            ArrayNode repartitionsNode = MAPPER.createArrayNode();

            List<Map.Entry<String, List<Integer>>> entryList =
                    new ArrayList<>(milestone.getRepartition().entrySet());

            entryList.sort(Comparator.comparingInt((Map.Entry<String,
                            List<Integer>> e) -> e.getValue().size())
                    .thenComparing(Map.Entry::getKey));

            for (Map.Entry<String, List<Integer>> repartitionEntry : entryList) {
                ObjectNode repartitionNode = MAPPER.createObjectNode();
                String dev = repartitionEntry.getKey();
                List<Integer> tickets = repartitionEntry.getValue();
                tickets.sort(Comparator.naturalOrder());
                repartitionNode.put("developer", dev);
                repartitionNode.set("assignedTickets", MAPPER.valueToTree(tickets));
                repartitionsNode.add(repartitionNode);
            }

            milestoneNode.set("repartition", repartitionsNode);

            milestonesArray.add(milestoneNode);
        }
        addOutput(commandInput, output, milestonesArray);
    }

    /**
     * Helper to append milestones to the output array.
     * @param input parsed command input
     * @param output JSON array to append results to
     * @param milestonesArray array of milestone nodes to include
     */
    public void addOutput(final CommandInput input,
                          final ArrayNode output,
                          final ArrayNode milestonesArray) {
        ObjectNode outputNode = MAPPER.createObjectNode();
        outputNode.put("command", input.getCommand());
        outputNode.put("username", input.getUsername());
        outputNode.put("timestamp", input.getTimestamp());
        outputNode.set("milestones", milestonesArray);
        output.add(outputNode);
    }
}
