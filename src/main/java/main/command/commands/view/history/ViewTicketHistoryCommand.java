package main.command.commands.view.history;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.command.Command;
import main.command.commands.view.history.viewtickethistorystrategy.actionoutputstrategy.*;
import main.command.commands.view.history.viewtickethistorystrategy.tickethistorystrategy.DeveloperHistoryStrategy;
import main.command.commands.view.history.viewtickethistorystrategy.tickethistorystrategy.HistoryFilteringStrategy;
import main.command.commands.view.history.viewtickethistorystrategy.tickethistorystrategy.ManagerHistoryStrategy;
import main.globals.commandenums.ErrorMessages;
import main.database.Database;
import main.fileio.CommandInput;
import main.tickets.Ticket;
import main.tickets.actions.Action;
import main.users.User;

import java.util.Comparator;
import java.util.List;

import static main.App.MAPPER;
import static main.globals.commandenums.CommandType.VIEW_TICKET_HISTORY;

/**
 * Command to view ticket history for developers and managers.
 */
public class ViewTicketHistoryCommand extends Command {

    /**
     * Execute view ticket history command and append ticket history array to output.
     * @param input parsed command input
     * @param output JSON array to append results to
     */
    @Override
    public void execute(CommandInput input, ArrayNode output) {
        Database db = Database.getInstance();

        if (db.getUserByUsername(input.getUsername()) == null) {
            addErrorOutput(input, output,
                    String.format(ErrorMessages.USER_NOT_FOUND.getErrorMessage(),
                            input.getUsername()));
            return;
        }

        User user = db.getUserByUsername(input.getUsername());


        HistoryFilteringStrategy strategy = switch (user.getRole()) {
            case DEVELOPER -> new DeveloperHistoryStrategy();
            case MANAGER ->  new ManagerHistoryStrategy();
            default -> null;
        };

        if (strategy == null) {
            addErrorOutput(input, output, ErrorMessages.REPORTERS_NOT_ALLOWED.getErrorMessage());
            return;
        }

        List<Ticket> tickets = strategy.getTickets(user);
        tickets.sort(Comparator.comparing(Ticket::getCreatedAt).thenComparing(Ticket::getId));

        ArrayNode ticketHistoryArray = MAPPER.createArrayNode();
        for (Ticket ticket : tickets) {
            ObjectNode ticketNode = MAPPER.createObjectNode();

            ticketNode.put("id", ticket.getId());
            ticketNode.put("title", ticket.getTitle());
            ticketNode.put("status", ticket.getStatus().getStatusName());

            ArrayNode actionsArray = MAPPER.createArrayNode();
            for (Action action : ticket.getActions()) {
                ActionOutputStrategy outputStrategy = switch (action.getActionType()) {
                    case STATUS_CHANGED -> new StatusChangedActionOutputStrategy();
                    case ASSIGNED -> new AssignedActionOutputStrategy();
                    case DE_ASSIGNED -> new DeassignedActionOutputStrategy();
                    case REMOVED_FROM_DEV -> new RemovedFromDevActionOutputStrategy();
                    case ADDED_TO_MILESTONE -> new AddedToMilestoneActionOutputStrategy();
                };
                actionsArray.add(outputStrategy.getActionNode(action));
            }

            ticketNode.set("actions", actionsArray);

            ArrayNode commentsArray = MAPPER.createArrayNode();
            for (Ticket.Comment comment : ticket.getComments()) {
                ObjectNode commentNode = MAPPER.createObjectNode();
                commentNode.put("author", comment.author());
                commentNode.put("content", comment.comment());
                commentNode.put("createdAt", comment.timestamp());
                commentsArray.add(commentNode);
            }

            ticketNode.set("comments", commentsArray);
            ticketHistoryArray.add(ticketNode);
        }

        addOutput(input, output, ticketHistoryArray);
    }

    /**
     * Helper to append ticket history to the output array.
     * @param input parsed command input
     * @param output JSON array to append results to
     * @param ticketHistoryArray ticket history array to include
     */
    public void addOutput(final CommandInput input,
                          final ArrayNode output,
                          final ArrayNode ticketHistoryArray) {
        node.put("command", VIEW_TICKET_HISTORY.getName());
        node.put("username", input.getUsername());
        node.put("timestamp", input.getTimestamp());
        node.set("ticketHistory", ticketHistoryArray);
        output.add(node);
    }
}
