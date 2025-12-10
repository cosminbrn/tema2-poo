package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.command.Command;
import main.command.commands.efficiencystrategy.BugTicketsEfficiencyStrategy;
import main.command.commands.efficiencystrategy.FeatureRequestEfficiencyStrategy;
import main.command.commands.efficiencystrategy.FeedbackTicketsEfficiencyStrategy;
import main.command.enums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.tickets.BugTicket;
import main.tickets.FeatureRequestTicket;
import main.tickets.Ticket;
import main.tickets.UIFeedbackTicket;
import main.users.Manager;
import main.users.enums.Role;

import java.util.ArrayList;
import java.util.List;

import static main.App.MAPPER;
import static main.command.enums.CommandType.GENERATE_RESOLUTION_EFFICIENCY_REPORT;

public class GenerateResolutionEfficiencyReportCommand extends Command {
    @Override
    public void execute(CommandInput commandInput, ArrayNode output) {
        Database db = Database.getInstance();

        if (db.getUserByUsername(commandInput.getUsername()) == null) {
            addErrorOutput(commandInput, output, String.format(ErrorMessages.USER_NOT_FOUND.getErrorMessage(), commandInput.getUsername()));
            return;
        }

        Role role = db.getUserByUsername(commandInput.getUsername()).getRole();
        if (role != Role.MANAGER) {
            addErrorOutput(commandInput, output, String.format(ErrorMessages.REQUIRED_ROLE_MANAGER.getErrorMessage(), role.getRoleName().toUpperCase()));
            return;
        }

        Manager manager = (Manager) db.getUserByUsername(commandInput.getUsername());

        List<Ticket> validTickets = db.getClosedResolvedTickets();
        List<BugTicket> bugTickets = new ArrayList<>();
        List<FeatureRequestTicket> featureTickets = new ArrayList<>();
        List<UIFeedbackTicket> uiFeedbackTickets = new ArrayList<>();

        for (Ticket ticket : validTickets) {
            switch (ticket.getType().getTypeName()) {
                case "BugTicket" -> bugTickets.add((BugTicket) ticket);
                case "FeatureRequestTicket" -> featureTickets.add((FeatureRequestTicket) ticket);
                case "UIFeedbackTicket" -> uiFeedbackTickets.add((UIFeedbackTicket) ticket);
            }
        }

        ObjectNode report = MAPPER.createObjectNode();
        report.put("totalTickets", validTickets.size());

        ObjectNode ticketsByType = MAPPER.createObjectNode();
        ticketsByType.put("BUG", bugTickets.size());
        ticketsByType.put("FEATURE_REQUEST", featureTickets.size());
        ticketsByType.put("UI_FEEDBACK", uiFeedbackTickets.size());
        report.set("ticketsByType", ticketsByType);

        int lowCount = 0, mediumCount = 0, highCount = 0, criticalCount = 0;
        for (Ticket ticket : validTickets) {
            switch (ticket.getBusinessPriority()) {
                case LOW -> lowCount++;
                case MEDIUM -> mediumCount++;
                case HIGH -> highCount++;
                case CRITICAL -> criticalCount++;
            }
        }

        ObjectNode ticketsByPriority = MAPPER.createObjectNode();
        ticketsByPriority.put("LOW", lowCount);
        ticketsByPriority.put("MEDIUM", mediumCount);
        ticketsByPriority.put("HIGH", highCount);
        ticketsByPriority.put("CRITICAL", criticalCount);
        report.set("ticketsByPriority", ticketsByPriority);

        BugTicketsEfficiencyStrategy bugStrategy = new BugTicketsEfficiencyStrategy();
        FeatureRequestEfficiencyStrategy featureStrategy = new FeatureRequestEfficiencyStrategy();
        FeedbackTicketsEfficiencyStrategy uiFeedBackStrategy = new FeedbackTicketsEfficiencyStrategy();

        ObjectNode efficiencyByType = MAPPER.createObjectNode();
        efficiencyByType.put("BUG", bugStrategy.calculateEfficiency(bugTickets));
        efficiencyByType.put("FEATURE_REQUEST", featureStrategy.calculateEfficiency(featureTickets));
        efficiencyByType.put("UI_FEEDBACK", uiFeedBackStrategy.calculateEfficiency(uiFeedbackTickets));
        report.set("efficiencyByType", efficiencyByType);

        addOutput(commandInput, output, report);
    }

    public void addOutput(final CommandInput input, final ArrayNode output, final ObjectNode report) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("command", GENERATE_RESOLUTION_EFFICIENCY_REPORT.getName());
        node.put("username", input.getUsername());
        node.put("timestamp", input.getTimestamp());
        node.set("report", report);
        output.add(node);
    }
}
