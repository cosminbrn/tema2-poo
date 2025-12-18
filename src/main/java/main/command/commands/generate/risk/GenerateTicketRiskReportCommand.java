package main.command.commands.generate.risk;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.command.Command;
import main.command.commands.generate.risk.riskstrategy.BugTicketsRiskStrategy;
import main.command.commands.generate.risk.riskstrategy.FeatureRequestTicketsRiskStrategy;
import main.command.commands.generate.risk.riskstrategy.FeedbackTicketsRiskStrategy;
import main.command.commands.generate.risk.riskstrategy.RiskScore;
import main.globals.commandenums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.tickets.BugTicket;
import main.tickets.FeatureRequestTicket;
import main.tickets.Ticket;
import main.tickets.UIFeedbackTicket;
import main.globals.userenums.Role;

import java.util.ArrayList;
import java.util.List;

import static main.App.MAPPER;

/**
 * Generate a ticket risk report for managers.
 */
public class GenerateTicketRiskReportCommand extends Command {

    /**
     * Execute the ticket risk report command and append the resulting report to output.
     * @param commandInput parsed command input
     * @param output JSON array to append results to
     */
    @Override
    public void execute(final CommandInput commandInput, final ArrayNode output) {
        if (!validateCommand(commandInput, output, Role.MANAGER)) {
            return;
        }

        List<Ticket> validTickets = db.getOpenInProgressTickets();
        List<BugTicket> bugTickets = new ArrayList<>();
        List<FeatureRequestTicket> featureTickets = new ArrayList<>();
        List<UIFeedbackTicket> uiFeedbackTickets = new ArrayList<>();

        for (Ticket ticket : validTickets) {
            switch (ticket.getType()) {
                case BUG -> bugTickets.add((BugTicket) ticket);
                case FEATURE_REQUEST -> featureTickets.add((FeatureRequestTicket) ticket);
                case UI_FEEDBACK -> uiFeedbackTickets.add((UIFeedbackTicket) ticket);
                default -> {
                    // ignore other types
                }
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
                default -> {
                    // ignore other priorities
                }
            }
        }

        ObjectNode ticketsByPriority = MAPPER.createObjectNode();
        ticketsByPriority.put("LOW", lowCount);
        ticketsByPriority.put("MEDIUM", mediumCount);
        ticketsByPriority.put("HIGH", highCount);
        ticketsByPriority.put("CRITICAL", criticalCount);
        report.set("ticketsByPriority", ticketsByPriority);

        BugTicketsRiskStrategy bugStrategy = new BugTicketsRiskStrategy();
        FeatureRequestTicketsRiskStrategy featureStrategy =
                new FeatureRequestTicketsRiskStrategy();
        FeedbackTicketsRiskStrategy uiFeedBackStrategy = new FeedbackTicketsRiskStrategy();

        ObjectNode riskByType = MAPPER.createObjectNode();
        String bugRisk =
                RiskScore.fromInt((int) bugStrategy.calculateImpact(bugTickets)).getName();
        String featureRisk =
                RiskScore.fromInt((int) featureStrategy.calculateImpact(featureTickets)).getName();
        String uiRisk =
                RiskScore.fromInt((int) uiFeedBackStrategy
                        .calculateImpact(uiFeedbackTickets)).getName();
        riskByType.put("BUG", bugRisk);
        riskByType.put("FEATURE_REQUEST", featureRisk);
        riskByType.put("UI_FEEDBACK", uiRisk);
        report.set("riskByType", riskByType);

        addOutput(commandInput, output, report);
    }

    /**
     * Helper to append the generated ticket risk report to the output.
     * @param commandInput parsed command input
     * @param output JSON array to append results to
     * @param report report object to attach
     */
    public void addOutput(final CommandInput commandInput,
                          final ArrayNode output,
                          final ObjectNode report) {
        node.put("command", commandInput.getCommand());
        node.put("username", commandInput.getUsername());
        node.put("timestamp", commandInput.getTimestamp());
        node.set("report", report);
        output.add(node);
    }
}
