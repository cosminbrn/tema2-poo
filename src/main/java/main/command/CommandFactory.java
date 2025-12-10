package main.command;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.commands.*;
import main.command.enums.CommandType;
import main.fileio.CommandInput;

public class CommandFactory {
    private CommandFactory() {

    }

    public static Command createCommand(CommandInput commandInput, ArrayNode output) {
        CommandType type = CommandType.fromString(commandInput.getCommand());
        return switch (type) {
            case CREATE_MILESTONE -> new CreateMilestoneCommand();
            case REPORT_TICKET -> new ReportTicketCommand();
            case VIEW_TICKETS -> new ViewTicketsCommand();
            case LOST_INVESTORS -> new LostInvestorsCommand();
            case VIEW_MILESTONES -> new ViewMilestonesCommand();
            case ASSIGN_TICKET ->  new AssignTicketCommand();
            case UNDO_ASSIGN_TICKET -> new UndoAssignTicketCommand();
            case VIEW_ASSIGNED_TICKETS -> new ViewAssignedTicketsCommand();
            case ADD_COMMENT ->  new AddCommentCommand();
            case UNDO_ADD_COMMENT -> new UndoAddCommentCommand();
            case CHANGE_STATUS -> new ChangeStatusCommand();
            case UNDO_CHANGE_STATUS -> new UndoChangeStatusCommand();
            case VIEW_TICKET_HISTORY -> new ViewTicketHistoryCommand();
            case VIEW_NOTIFICATIONS -> new ViewNotificationsCommand();
            case GENERATE_PERFORMANCE_REPORT -> new GeneratePerformanceReportCommand();
            case GENERATE_RESOLUTION_EFFICIENCY_REPORT -> new GenerateResolutionEfficiencyReportCommand();
            case GENERATE_CUSTOMER_IMPACT_REPORT -> new GenerateCustomerImpactReportCommand();
            case GENERATE_TICKET_RISK_REPORT -> new GenerateTicketRiskReportCommand();
            case APP_STABILITY_REPORT -> new AppStabilityReportCommand();
            default -> null;
        };
    }
}
