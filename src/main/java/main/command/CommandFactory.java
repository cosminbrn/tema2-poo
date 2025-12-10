package main.command;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.commands.AddCommentCommand;
import main.command.commands.AppStabilityReportCommand;
import main.command.commands.AssignTicketCommand;
import main.command.commands.ChangeStatusCommand;
import main.command.commands.CreateMilestoneCommand;
import main.command.commands.GenerateCustomerImpactReportCommand;
import main.command.commands.GeneratePerformanceReportCommand;
import main.command.commands.GenerateResolutionEfficiencyReportCommand;
import main.command.commands.GenerateTicketRiskReportCommand;
import main.command.commands.LostInvestorsCommand;
import main.command.commands.ReportTicketCommand;
import main.command.commands.UndoAddCommentCommand;
import main.command.commands.UndoAssignTicketCommand;
import main.command.commands.UndoChangeStatusCommand;
import main.command.commands.ViewAssignedTicketsCommand;
import main.command.commands.ViewMilestonesCommand;
import main.command.commands.ViewNotificationsCommand;
import main.command.commands.ViewTicketHistoryCommand;
import main.command.commands.ViewTicketsCommand;
import main.command.enums.CommandType;
import main.fileio.CommandInput;

import java.util.Objects;

/**
 * Factory that creates appropriate Command instances for a parsed input.
 */
public final class CommandFactory {
    private CommandFactory() {

    }

    /**
     * Create a Command instance for the provided input.
     *
     * @param commandInput the parsed command input
     * @param output       the output array (kept for compatibility)
     * @return a Command instance or null if the command type is unknown
     */
    public static Command createCommand(final CommandInput commandInput,
                                        final ArrayNode output) {
        // reference output to avoid unused-parameter warnings
        Objects.requireNonNull(output, "output must not be null");

        CommandType type = CommandType.fromString(commandInput.getCommand());
        if (type == null) {
            return null;
        }

        return switch (type) {
            case CREATE_MILESTONE -> new CreateMilestoneCommand();
            case REPORT_TICKET -> new ReportTicketCommand();
            case VIEW_TICKETS -> new ViewTicketsCommand();
            case LOST_INVESTORS -> new LostInvestorsCommand();
            case VIEW_MILESTONES -> new ViewMilestonesCommand();
            case ASSIGN_TICKET -> new AssignTicketCommand();
            case UNDO_ASSIGN_TICKET -> new UndoAssignTicketCommand();
            case VIEW_ASSIGNED_TICKETS -> new ViewAssignedTicketsCommand();
            case ADD_COMMENT -> new AddCommentCommand();
            case UNDO_ADD_COMMENT -> new UndoAddCommentCommand();
            case CHANGE_STATUS -> new ChangeStatusCommand();
            case UNDO_CHANGE_STATUS -> new UndoChangeStatusCommand();
            case VIEW_TICKET_HISTORY -> new ViewTicketHistoryCommand();
            case VIEW_NOTIFICATIONS -> new ViewNotificationsCommand();
            case GENERATE_PERFORMANCE_REPORT -> new GeneratePerformanceReportCommand();
            case GENERATE_RESOLUTION_EFFICIENCY_REPORT ->
                    new GenerateResolutionEfficiencyReportCommand();
            case GENERATE_CUSTOMER_IMPACT_REPORT -> new GenerateCustomerImpactReportCommand();
            case GENERATE_TICKET_RISK_REPORT -> new GenerateTicketRiskReportCommand();
            case APP_STABILITY_REPORT -> new AppStabilityReportCommand();
        };
    }
}
