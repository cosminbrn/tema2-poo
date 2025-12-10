package main.command.commands.view.history.viewtickethistorystrategy.actionoutputstrategy;

import com.fasterxml.jackson.databind.node.ObjectNode;
import main.tickets.actions.Action;
import main.tickets.actions.RemoveFromDevAction;

import static main.App.MAPPER;

/**
 * Output strategy for REMOVED_FROM_DEV actions.
 */
public class RemovedFromDevActionOutputStrategy implements ActionOutputStrategy {

    /**
     * Convert RemoveFromDevAction into JSON node.
     * @param action action instance
     * @return JSON node representing the action
     */
    @Override
    public ObjectNode getActionNode(final Action action) {
        RemoveFromDevAction newAction = (RemoveFromDevAction) action;
        ObjectNode actionNode = MAPPER.createObjectNode();
        actionNode.put("from", newAction.getFrom());
        actionNode.put("by", action.getBy());
        actionNode.put("timestamp", action.getTimestamp());
        actionNode.put("action", "REMOVED_FROM_DEV");
        return actionNode;
    }
}
