package main.command.commands.generate.performance;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.command.Command;
import main.command.commands.generate.performance.performancestrategy.JuniorPerformanceStrategy;
import main.command.commands.generate.performance.performancestrategy.MidPerformanceStrategy;
import main.command.commands.generate.performance.performancestrategy.PerformanceScoreStrategy;
import main.command.commands.generate.performance.performancestrategy.SeniorPerformanceStrategy;
import main.globals.commandenums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.tickets.Ticket;
import main.users.Developer;
import main.users.Manager;
import main.globals.userenums.Role;
import main.globals.userenums.Seniority;

import java.util.Comparator;
import java.util.List;

import static main.App.MAPPER;
import static main.globals.commandenums.CommandType.GENERATE_PERFORMANCE_REPORT;

/**
 * Generate a performance report for managers.
 */
public class GeneratePerformanceReportCommand extends Command {
    /**
     * Execute the performance report command and append reports to output.
     * @param commandInput parsed command input
     * @param output JSON array to append results to
     */
    @Override
    public void execute(final CommandInput commandInput, final ArrayNode output) {
        Database db = Database.getInstance();

        if (db.getUserByUsername(commandInput.getUsername()) == null) {
            String msg = String.format(ErrorMessages.USER_NOT_FOUND.getErrorMessage(),
                            commandInput.getUsername());
            addErrorOutput(commandInput, output, msg);
            return;
        }

        Role role = db.getUserByUsername(commandInput.getUsername()).getRole();
        if (role != Role.MANAGER) {
            String msg = String.format(ErrorMessages.REQUIRED_ROLE_MANAGER.getErrorMessage(),
                    role.getRoleName().toUpperCase());
            addErrorOutput(commandInput, output, msg);
            return;
        }

        Manager manager = (Manager) db.getUserByUsername(commandInput.getUsername());
        ArrayNode reports = MAPPER.createArrayNode();

        List<Developer> subordinates = manager.getSubordinateDevelopers();
        subordinates.sort(Comparator.comparing(Developer::getUsername));

        for (Developer dev : subordinates) {
            Seniority seniority = dev.getSeniority();

            PerformanceScoreStrategy strategy = switch (seniority) {
                case JUNIOR -> new JuniorPerformanceStrategy();
                case MID -> new MidPerformanceStrategy();
                case SENIOR -> new SeniorPerformanceStrategy();
            };

            List<Ticket> devTickets =
                    dev.getClosedTicketsFromLastMonth(commandInput.getTimestamp());

            double performanceScore = 0.0;
            if (!devTickets.isEmpty()) {
                performanceScore = strategy.calculatePerformanceScore(dev,
                        commandInput.getTimestamp());
            }

            double averageResolutionTime =
                    PerformanceScoreStrategy.averageResolutionTime(devTickets);
            int closedCount = devTickets.size();

            ObjectNode devReport = MAPPER.createObjectNode();
            devReport.put("username", dev.getUsername());
            devReport.put("closedTickets", closedCount);
            devReport.put("averageResolutionTime", averageResolutionTime);
            devReport.put("performanceScore", performanceScore);
            devReport.put("seniority", seniority.getName());

            reports.add(devReport);
        }

        addOutput(commandInput, output, reports);
    }

    /**
     * Helper to append the generated performance reports to the output.
     * @param input parsed command input
     * @param output JSON array to append results to
     * @param reports reports array to include in the response
     */
    public void addOutput(final CommandInput input,
                          final ArrayNode output, final ArrayNode reports) {
         node.put("command", GENERATE_PERFORMANCE_REPORT.getName());
         node.put("username", input.getUsername());
         node.put("timestamp", input.getTimestamp());
         node.set("report", reports);
         output.add(node);
     }
 }
