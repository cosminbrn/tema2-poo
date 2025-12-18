package main.command.commands.generate.impact;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.command.Command;
import main.command.commands.generate.impact.impactstrategy.BugTicketsImpactStrategy;
import main.command.commands.generate.impact.impactstrategy.FeatureRequestTicketsImpactStrategy;
import main.command.commands.generate.impact.impactstrategy.FeedbackTicketsImpactStrategy;
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
 * Generate a customer impact report for managers.
 */
public class GenerateCustomerImpactReportCommand extends Command {

    /**
     * Execute the command and append the resulting JSON node(s) to the provided output array.
     * @param commandInput the parsed command input
     * @param output the array node to append results to
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
                    // unknown type - ignore
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
                    // unknown priority - ignore
                }
            }
        }

        ObjectNode ticketsByPriority = MAPPER.createObjectNode();
        ticketsByPriority.put("LOW", lowCount);
        ticketsByPriority.put("MEDIUM", mediumCount);
        ticketsByPriority.put("HIGH", highCount);
        ticketsByPriority.put("CRITICAL", criticalCount);
        report.set("ticketsByPriority", ticketsByPriority);

        BugTicketsImpactStrategy bugStrategy =
                new BugTicketsImpactStrategy();
        FeatureRequestTicketsImpactStrategy featureStrategy =
                new FeatureRequestTicketsImpactStrategy();
        FeedbackTicketsImpactStrategy uiFeedBackStrategy =
                new FeedbackTicketsImpactStrategy();

        ObjectNode customerImpactByType = MAPPER.createObjectNode();
        customerImpactByType.put("BUG", bugStrategy.calculateImpact(bugTickets));
        customerImpactByType.put("FEATURE_REQUEST",
                featureStrategy.calculateImpact(featureTickets));
        customerImpactByType.put("UI_FEEDBACK",
                uiFeedBackStrategy.calculateImpact(uiFeedbackTickets));
        report.set("customerImpactByType", customerImpactByType);

        addOutput(commandInput, output, report);
    }

    /**
     * Helper to append a successfully generated report to the provided output array.
     *
     * @param commandInput the original command input
     * @param output       the JSON array to append the result to
     * @param report       the report object to attach
     */
    public void addOutput(final CommandInput commandInput, final ArrayNode output,
                          final ObjectNode report) {
        node.put("command", commandInput.getCommand());
        node.put("username", commandInput.getUsername());
        node.put("timestamp", commandInput.getTimestamp());
        node.set("report", report);
        output.add(node);
    }
}
