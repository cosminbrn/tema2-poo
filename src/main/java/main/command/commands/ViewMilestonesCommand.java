package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.command.Command;
import main.command.commands.viewmilestonesstrategy.DeveloperMilestoneViewStrategy;
import main.command.commands.viewmilestonesstrategy.ManagerMilestoneViewStrategy;
import main.command.commands.viewmilestonesstrategy.MilestoneFilteringStrategy;
import main.command.enums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.milestones.Milestone;
import main.users.User;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static main.App.MAPPER;

public class ViewMilestonesCommand extends Command {


    @Override
    public void execute(CommandInput input, ArrayNode output) {
        Database db = Database.getInstance();

        if (db.getUserByUsername(input.getUsername()) == null) {
            addErrorOutput(input, output, String.format(ErrorMessages.USER_NOT_FOUND.getErrorMessage(), input.getUsername()));
            return;
        }

        User user = db.getUserByUsername(input.getUsername());


        MilestoneFilteringStrategy strategy = switch (user.getRole()) {
            case DEVELOPER -> new DeveloperMilestoneViewStrategy();
            case MANAGER -> new ManagerMilestoneViewStrategy();
            default -> throw new IllegalStateException("Unexpected value: " + user.getRole());
        };

        List<Milestone> milestones = strategy.getMilestones(user);
        milestones.sort(Comparator.comparing(Milestone::getDueDate).thenComparing(Milestone::getName));
        ArrayNode milestonesArray = MAPPER.createArrayNode();
        for (Milestone milestone : milestones) {
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
            milestoneNode.put("daysUntilDue", milestone.calculateDaysUntilDue(LocalDate.parse(input.getTimestamp())));
            milestoneNode.put("overdueBy", milestone.calculateOverdueBy(LocalDate.parse(input.getTimestamp())));
            milestoneNode.set("openTickets", MAPPER.valueToTree(milestone.getOpenTickets()));
            milestoneNode.set("closedTickets", MAPPER.valueToTree(milestone.getClosedTickets()));
            milestoneNode.put("completionPercentage", milestone.calculateCompletionPercentage());

            ArrayNode repartitionsNode = MAPPER.createArrayNode();
            for (Map.Entry<String, List<Integer>> repartitionEntry : milestone.getRepartition().entrySet()) {
                ObjectNode repartitionNode = MAPPER.createObjectNode();
                String dev = repartitionEntry.getKey();
                List<Integer> tickets = repartitionEntry.getValue();
                repartitionNode.put("developer", dev);
                repartitionNode.set("assignedTickets", MAPPER.valueToTree(tickets));
                repartitionsNode.add(repartitionNode);
            }

            milestoneNode.set("repartition", repartitionsNode);

            milestonesArray.add(milestoneNode);
        }
        addOutput(input, output, milestonesArray);
    }

    public void addOutput(CommandInput input, ArrayNode output, ArrayNode milestonesArray) {
        ObjectNode outputNode = MAPPER.createObjectNode();
        outputNode.put("command", input.getCommand());
        outputNode.put("username", input.getUsername());
        outputNode.put("timestamp", input.getTimestamp());
        outputNode.set("milestones", milestonesArray);
        output.add(outputNode);
    }
}
