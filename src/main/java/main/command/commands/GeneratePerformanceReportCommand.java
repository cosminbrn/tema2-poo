package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.command.Command;
import main.command.commands.performancestrategy.JuniorPerformanceStrategy;
import main.command.commands.performancestrategy.MidPerformanceStrategy;
import main.command.commands.performancestrategy.PerformanceScoreStrategy;
import main.command.commands.performancestrategy.SeniorPerformanceStrategy;
import main.command.enums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.tickets.Ticket;
import main.users.Developer;
import main.users.Manager;
import main.users.User;
import main.users.enums.Role;
import main.users.enums.Seniority;

import java.util.Comparator;
import java.util.List;

import static main.App.MAPPER;
import static main.command.enums.CommandType.GENERATE_PERFORMANCE_REPORT;

public class GeneratePerformanceReportCommand extends Command {
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

            List<Ticket> validTickets = dev.getClosedTicketsFromLastMonth(commandInput.getTimestamp());

            double performanceScore = 0.0;
            if (!validTickets.isEmpty()) {
                performanceScore = strategy.calculatePerformanceScore(dev, commandInput.getTimestamp());
            }

            double averageResolutionTime = PerformanceScoreStrategy.averageResolutionTime(dev.getClosedTicketsFromLastMonth(commandInput.getTimestamp()));

            ObjectNode devReport = MAPPER.createObjectNode();
            devReport.put("username", dev.getUsername());
            devReport.put("closedTickets", dev.getClosedTicketsFromLastMonth(commandInput.getTimestamp()).size());
            devReport.put("averageResolutionTime", averageResolutionTime);
            devReport.put("performanceScore", performanceScore);
            devReport.put("seniority", seniority.getName());

            reports.add(devReport);
        }

        addOutput(commandInput, output, reports);
    }

    public void addOutput(final CommandInput input, final ArrayNode output, final ArrayNode reports) {
        node.put("command", GENERATE_PERFORMANCE_REPORT.getName());
        node.put("username", input.getUsername());
        node.put("timestamp", input.getTimestamp());
        node.set("report", reports);
        output.add(node);
    }
}
