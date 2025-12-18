package main.command.commands.view;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.globals.commandenums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.users.Developer;
import main.globals.userenums.Role;
import main.users.User;

import static main.App.MAPPER;

/**
 * Command to view and clear notifications for a developer or manager.
 */
public class ViewNotificationsCommand extends Command {
    /**
     * Execute view notifications command and append notifications array to output.
     * @param commandInput parsed command input
     * @param output JSON array to append results to
     */
    @Override
    public void execute(final CommandInput commandInput, final ArrayNode output) {
        if (!validateCommand(commandInput, output, Role.DEVELOPER)) {
            return;
        }

        ArrayNode notifications = MAPPER.createArrayNode();

        Developer dev = (Developer) db.getUserByUsername(commandInput.getUsername());
        if (dev.getNotifications() != null) {
            for (String notification : dev.getNotifications()) {
                notifications.add(notification);
            }
        }
        dev.clearNotifications();

        addOutput(commandInput, output, notifications);
    }

    /**
     * Helper to append notifications to the output array.
     * @param input parsed command input
     * @param output JSON array to append results to
     * @param notifications notifications array to include in the response
     */
    public void addOutput(final CommandInput input,
                          final ArrayNode output,
                          final ArrayNode notifications) {
        node.put("command", input.getCommand());
        node.put("username", input.getUsername());
        node.put("timestamp", input.getTimestamp());
        node.set("notifications", notifications);
        output.add(node);
    }
}
