package main.command;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.commands.LostInvestorsCommand;
import main.command.commands.ReportTicketCommand;
import main.command.commands.ViewTicketsCommand;
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
            case CREATE_MILESTONE -> null;
            case REPORT_TICKET -> new ReportTicketCommand();
            case VIEW_TICKETS -> new ViewTicketsCommand();
            case LOST_INVESTORS -> new LostInvestorsCommand();
        };
    }
}
