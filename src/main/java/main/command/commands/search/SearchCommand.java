package main.command.commands.search;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.globals.commandenums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.users.User;

public class SearchCommand extends Command {
    @Override
    public void execute(CommandInput commandInput, ArrayNode output) {
        Database db = Database.getInstance();

        User currentUser = db.getUserByUsername(commandInput.getUsername());
        if (currentUser == null) {
            addErrorOutput(commandInput, output,
                    String.format(ErrorMessages.USER_NOT_FOUND.getErrorMessage(),
                            commandInput.getUsername()));
            return;
        }
    }
}
