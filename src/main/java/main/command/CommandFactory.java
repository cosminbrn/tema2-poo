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
        assert type != null;
        return switch (type) {
            //case CREATE_MILESTONE -> new CreateMilestoneCommand(commandInput);
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
            default -> null;
        };
    }
}
