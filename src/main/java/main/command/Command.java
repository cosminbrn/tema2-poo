package main.command;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.database.Database;
import main.engine.Engine;
import main.fileio.CommandInput;
import main.globals.commandenums.ErrorMessages;
import main.globals.userenums.Role;
import main.users.User;

import java.util.Arrays;
import java.util.List;

import static main.App.MAPPER;

/**
 * Base command class: provides a convenience JSON node and a helper to append
 * error outputs to the shared result array.
 */
public abstract class Command {
    protected ObjectNode node = MAPPER.createObjectNode();
    protected Database db = Database.getInstance();
    protected Engine engine = Engine.getInstance();

    /**
     * Execute the command and append any outputs to the provided output array.
     * @param commandInput the parsed command input
     * @param output the array to append output nodes to
     */
    public abstract void execute(CommandInput commandInput, ArrayNode output);

    /**
     * Append an error object for the provided input to the output array.
     *
     * @param input        the original command input
     * @param output       the array to append the error node to
     * @param errorMessage the error message to include
     */
    public void addErrorOutput(final CommandInput input,
                               final ArrayNode output,
                               final String errorMessage) {
        node.put("command", input.getCommand());
        node.put("username", input.getUsername());
        node.put("timestamp", input.getTimestamp());
        node.put("error", errorMessage);
        output.add(node);
    }

    public boolean validateCommand(final CommandInput input, final ArrayNode output, Role... requiredRoles) {
        User currentUser = db.getUserByUsername(input.getUsername());

        if (currentUser == null) {
            addErrorOutput(input, output,
                    String.format(ErrorMessages.USER_NOT_FOUND.getErrorMessage(),
                            input.getUsername()));
            return false;
        }

        Role userRole = currentUser.getRole();

        for (Role role : requiredRoles) {
            if (userRole == role) {
                return true;
            }
        }
        List<Role> roleList = Arrays.asList(requiredRoles);
        if (roleList.size() == 1 && roleList.getFirst() == Role.MANAGER) {
            addErrorOutput(input, output, String.format(
                    ErrorMessages.REQUIRED_ROLE_MANAGER.getErrorMessage(),
                    userRole.getRoleName().toUpperCase()));
            return false;
        } else if (roleList.size() == 1 && roleList.getFirst() == Role.DEVELOPER) {
            addErrorOutput(input, output, String.format(
                    ErrorMessages.REQUIRED_ROLE_DEVELOPER.getErrorMessage(),
                    userRole.getRoleName().toUpperCase()));
            return false;
        } else if (!roleList.isEmpty()) {
            addErrorOutput(input, output,
                    ErrorMessages.REPORTERS_NOT_ALLOWED.getErrorMessage());
            return false;
        }

        return true;
    }
}
