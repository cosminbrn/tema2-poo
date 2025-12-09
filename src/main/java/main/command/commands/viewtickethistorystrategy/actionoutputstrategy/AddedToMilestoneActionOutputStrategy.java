package main.command.commands.viewtickethistorystrategy.actionoutputstrategy;

import com.fasterxml.jackson.databind.node.ObjectNode;
import main.tickets.actions.Action;
import main.tickets.actions.AddToMilestoneAction;

import static main.App.MAPPER;

public class AddedToMilestoneActionOutputStrategy implements ActionOutputStrategy {

    @Override
    public ObjectNode getActionNode(Action action) {
        AddToMilestoneAction newAction = (AddToMilestoneAction) action;
        ObjectNode actionNode = MAPPER.createObjectNode();
        actionNode.put("milestone", newAction.getMilestone());
        actionNode.put("by", newAction.getBy());
        actionNode.put("timestamp", newAction.getTimestamp());
        actionNode.put("action", "ADDED_TO_MILESTONE");
        return actionNode;

    }
}
