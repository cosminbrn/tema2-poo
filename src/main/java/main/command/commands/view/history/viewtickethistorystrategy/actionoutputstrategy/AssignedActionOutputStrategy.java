package main.command.commands.view.history.viewtickethistorystrategy.actionoutputstrategy;

import com.fasterxml.jackson.databind.node.ObjectNode;
import main.tickets.actions.Action;

import static main.App.MAPPER;

/**
 * Output strategy for ASSIGNED actions.
 */
public class AssignedActionOutputStrategy implements ActionOutputStrategy {

    /**
     * Convert assign action into JSON node.
     * @param action action instance
     * @return JSON node representing the action
     */
    @Override
    public ObjectNode getActionNode(final Action action) {
        ObjectNode actionNode = MAPPER.createObjectNode();
        actionNode.put("by", action.getBy());
        actionNode.put("timestamp", action.getTimestamp());
        actionNode.put("action", "ASSIGNED");
        return actionNode;
    }
}
