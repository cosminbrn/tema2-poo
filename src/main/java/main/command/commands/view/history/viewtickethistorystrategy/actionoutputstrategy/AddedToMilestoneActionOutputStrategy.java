package main.command.commands.view.history.viewtickethistorystrategy.actionoutputstrategy;

import com.fasterxml.jackson.databind.node.ObjectNode;
import main.tickets.actions.Action;
import main.tickets.actions.AddToMilestoneAction;

import static main.App.MAPPER;

/**
 * Output strategy for ADDED_TO_MILESTONE actions.
 */
public class AddedToMilestoneActionOutputStrategy implements ActionOutputStrategy {

    /**
     * Convert AddToMilestoneAction into an ObjectNode.
     * @param action action instance
     * @return JSON node representing the action
     */
    @Override
    public ObjectNode getActionNode(final Action action) {
        AddToMilestoneAction newAction = (AddToMilestoneAction) action;
        ObjectNode actionNode = MAPPER.createObjectNode();
        actionNode.put("milestone", newAction.getMilestone());
        actionNode.put("by", newAction.getBy());
        actionNode.put("timestamp", newAction.getTimestamp());
        actionNode.put("action", "ADDED_TO_MILESTONE");
        return actionNode;

    }
}
