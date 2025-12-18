package main.command.commands.generate.efficiency;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.command.Command;
import main.command.commands.generate.efficiency.efficiencystrategy.BugTicketsEfficiencyStrategy;
import main.command.commands.generate.efficiency.efficiencystrategy.FeatureRequestEfficiencyStrategy;
import main.command.commands.generate.efficiency.efficiencystrategy.FeedbackTicketsEfficiencyStrategy;
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
import static main.globals.commandenums.CommandType.GENERATE_RESOLUTION_EFFICIENCY_REPORT;

/**
 * Generate a resolution efficiency report for managers.
 */
public class GenerateResolutionEfficiencyReportCommand extends Command {
    /**
     * Execute the resolution efficiency report command and
     * append the resulting JSON node to output.
     * @param commandInput the parsed command input
     * @param output the array node to append results to
     */
    @Override
    public void execute(final CommandInput commandInput, final ArrayNode output) {
        if (!validateCommand(commandInput, output, Role.MANAGER)) {
            return;
        }

        List<Ticket> validTickets = db.getClosedResolvedTickets();
        List<BugTicket> bugTickets = new ArrayList<>();
        List<FeatureRequestTicket> featureTickets = new ArrayList<>();
        List<UIFeedbackTicket> uiFeedbackTickets = new ArrayList<>();

        for (Ticket ticket : validTickets) {
            switch (ticket.getType()) {
                case BUG -> bugTickets.add((BugTicket) ticket);
                case FEATURE_REQUEST -> featureTickets.add((FeatureRequestTicket) ticket);
                case UI_FEEDBACK -> uiFeedbackTickets.add((UIFeedbackTicket) ticket);
                default -> {

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
            switch (ticket.getPreviousBusinessPriority()) {
                case LOW -> lowCount++;
                case MEDIUM -> mediumCount++;
                case HIGH -> highCount++;
                case CRITICAL -> criticalCount++;
                default -> {
                    // ignore
                }
            }
        }

        ObjectNode ticketsByPriority = MAPPER.createObjectNode();
        ticketsByPriority.put("LOW", lowCount);
        ticketsByPriority.put("MEDIUM", mediumCount);
        ticketsByPriority.put("HIGH", highCount);
        ticketsByPriority.put("CRITICAL", criticalCount);
        report.set("ticketsByPriority", ticketsByPriority);

        BugTicketsEfficiencyStrategy bugStrategy = new BugTicketsEfficiencyStrategy();
        FeatureRequestEfficiencyStrategy featureStrategy =
                new FeatureRequestEfficiencyStrategy();
        FeedbackTicketsEfficiencyStrategy uiFeedBackStrategy =
                new FeedbackTicketsEfficiencyStrategy();

        double bugEff = bugStrategy.calculateEfficiency(bugTickets);
        double featureEff = featureStrategy.calculateEfficiency(featureTickets);
        double uiEff = uiFeedBackStrategy.calculateEfficiency(uiFeedbackTickets);

        ObjectNode efficiencyByType = MAPPER.createObjectNode();
        efficiencyByType.put("BUG", bugEff);
        efficiencyByType.put("FEATURE_REQUEST", featureEff);
        efficiencyByType.put("UI_FEEDBACK", uiEff);
        report.set("efficiencyByType", efficiencyByType);

        addOutput(commandInput, output, report);
    }

    /**
     * Helper to append the generated report to the output.
     * @param input parsed command input
     * @param output JSON array to append results to
     * @param report report object to attach
     */
    public void addOutput(final CommandInput input,
                          final ArrayNode output,
                          final ObjectNode report) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("command", GENERATE_RESOLUTION_EFFICIENCY_REPORT.getName());
        node.put("username", input.getUsername());
        node.put("timestamp", input.getTimestamp());
        node.set("report", report);
        output.add(node);
    }
}
