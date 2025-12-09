package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.command.enums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;

public class ViewTicketHistoryCommand extends Command {
    @Override
    public void execute(CommandInput input, ArrayNode output) {
        Database db = Database.getInstance();

        if (db.getUserByUsername(input.getUsername()) == null) {
            addErrorOutput(input, output, String.format(ErrorMessages.USER_NOT_FOUND.getErrorMessage(), input.getUsername()));
            return;
        }
    }
}
