package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.command.Command;
import main.command.commands.impactstrategy.BugTicketsImpactStrategy;
import main.command.commands.impactstrategy.FeatureRequestTicketsImpactStrategy;
import main.command.commands.impactstrategy.FeedbackTicketsImpactStrategy;
import main.command.commands.riskstrategy.BugTicketsRiskStrategy;
import main.command.commands.riskstrategy.FeatureRequestTicketsRiskStrategy;
import main.command.commands.riskstrategy.FeedbackTicketsRiskStrategy;
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

public class GenerateTicketRiskReportCommand extends Command {
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

        List<Ticket> validTickets = db.getOpenInProgressTickets();
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

        BugTicketsRiskStrategy bugStrategy = new BugTicketsRiskStrategy();
        FeatureRequestTicketsRiskStrategy featureStrategy = new FeatureRequestTicketsRiskStrategy();
        FeedbackTicketsRiskStrategy uiFeedBackStrategy = new FeedbackTicketsRiskStrategy();

        ObjectNode riskByType = MAPPER.createObjectNode();
        riskByType.put("BUG", bugStrategy.calculateImpact(bugTickets));
        riskByType.put("FEATURE_REQUEST", featureStrategy.calculateImpact(featureTickets));
        riskByType.put("UI_FEEDBACK", uiFeedBackStrategy.calculateImpact(uiFeedbackTickets));
        report.set("riskByType", riskByType);

        addOutput(commandInput, output, report);
    }

    public void addOutput(CommandInput commandInput, ArrayNode output, ObjectNode report) {
        node.put("command", commandInput.getCommand());
        node.put("username", commandInput.getUsername());
        node.put("timestamp", commandInput.getTimestamp());
        node.set("report", report);
        output.add(node);
    }

}
