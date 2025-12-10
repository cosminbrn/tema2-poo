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
import main.command.commands.riskstrategy.RiskScore;
import main.command.enums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.globals.Stability;
import main.tickets.BugTicket;
import main.tickets.FeatureRequestTicket;
import main.tickets.Ticket;
import main.tickets.UIFeedbackTicket;
import main.users.Manager;
import main.users.enums.Role;

import java.util.ArrayList;
import java.util.List;

import static main.App.MAPPER;
import static main.command.commands.riskstrategy.RiskScore.NEGLIGIBLE;
import static main.command.commands.riskstrategy.RiskScore.SIGNIFICANT;
import static main.globals.Stability.*;

/**
 * Command that generates an application stability report for managers.
 */
public class AppStabilityReportCommand extends Command {
    /**
     * Execute the stability report command and append the resulting JSON node.
     * @param commandInput parsed command input
     * @param output JSON array to append results to
     */
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
            switch (ticket.getType()) {
                case BUG -> bugTickets.add((BugTicket) ticket);
                case FEATURE_REQUEST -> featureTickets.add((FeatureRequestTicket) ticket);
                case UI_FEEDBACK -> uiFeedbackTickets.add((UIFeedbackTicket) ticket);
            }
        }

        ObjectNode report = MAPPER.createObjectNode();
        report.put("totalOpenTickets", validTickets.size());

        ObjectNode ticketsByType = MAPPER.createObjectNode();
        ticketsByType.put("BUG", bugTickets.size());
        ticketsByType.put("FEATURE_REQUEST", featureTickets.size());
        ticketsByType.put("UI_FEEDBACK", uiFeedbackTickets.size());
        report.set("openTicketsByType", ticketsByType);

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
        report.set("openTicketsByPriority", ticketsByPriority);

        BugTicketsRiskStrategy bugRiskStrategy = new BugTicketsRiskStrategy();
        FeatureRequestTicketsRiskStrategy featureRiskStrategy = new FeatureRequestTicketsRiskStrategy();
        FeedbackTicketsRiskStrategy uiFeedBackRiskStrategy = new FeedbackTicketsRiskStrategy();

        List<RiskScore> riskScores = new ArrayList<>();
        riskScores.add(RiskScore.fromInt((int) bugRiskStrategy.calculateImpact(bugTickets)));
        riskScores.add(RiskScore.fromInt((int) featureRiskStrategy.calculateImpact(featureTickets)));
        riskScores.add(RiskScore.fromInt((int) uiFeedBackRiskStrategy.calculateImpact(uiFeedbackTickets)));


        ObjectNode riskByType = MAPPER.createObjectNode();
        riskByType.put("BUG", riskScores.get(0).getName());
        riskByType.put("FEATURE_REQUEST", riskScores.get(1).getName());
        riskByType.put("UI_FEEDBACK", riskScores.get(2).getName());
        report.set("riskByType", riskByType);

        BugTicketsImpactStrategy bugStrategy = new BugTicketsImpactStrategy();
        FeatureRequestTicketsImpactStrategy featureStrategy = new FeatureRequestTicketsImpactStrategy();
        FeedbackTicketsImpactStrategy uiFeedBackStrategy = new FeedbackTicketsImpactStrategy();

        List<Double> impacts = new ArrayList<>();
        impacts.add(bugStrategy.calculateImpact(bugTickets));
        impacts.add(featureStrategy.calculateImpact(featureTickets));
        impacts.add(uiFeedBackStrategy.calculateImpact(uiFeedbackTickets));

        ObjectNode customerImpactByType = MAPPER.createObjectNode();
        customerImpactByType.put("BUG", impacts.get(0));
        customerImpactByType.put("FEATURE_REQUEST", impacts.get(1));
        customerImpactByType.put("UI_FEEDBACK", impacts.get(2));
        report.set("impactByType", customerImpactByType);

        Stability stability = PARTIALLY_STABLE;
        if (db.getOpenInProgressTickets().isEmpty()) {
            stability = STABLE;
        } else {
            int ok = 1;
            for (RiskScore score : riskScores) {
                if (score != NEGLIGIBLE) {
                    ok = 0;
                    break;
                }
            }
            for (Double impact : impacts) {
                if (impact >= 50.0) {
                    ok = 0;
                    break;
                }
            }

            if (ok == 1) {
                stability = STABLE;
            } else {
                for (RiskScore score : riskScores) {
                    if (score == SIGNIFICANT) {
                        stability = UNSTABLE;
                        break;
                    }
                }
            }
        }
        report.put("appStability", stability.getName());
        addOutput(commandInput, output, report);
    }

    /**
     * Helper to append a successfully generated report to the provided output
     * array.
     * @param commandInput the original command input (must not be null)
     * @param output the JSON array to append the result to (must not be null)
     * @param report the report object to attach (must not be null)
     */
    public void addOutput(CommandInput commandInput, ArrayNode output, ObjectNode report) {
        node.put("command", commandInput.getCommand());
        node.put("username", commandInput.getUsername());
        node.put("timestamp", commandInput.getTimestamp());
        node.set("report", report);
        output.add(node);
    }
}
