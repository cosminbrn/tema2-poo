package main.command.commands.viewtickethistorystrategy.actionoutputstrategy;

import com.fasterxml.jackson.databind.node.ObjectNode;
import main.tickets.actions.Action;
import main.tickets.actions.RemoveFromDevAction;

import static main.App.MAPPER;

public class RemovedFromDevActionOutputStrategy implements ActionOutputStrategy {

    @Override
    public ObjectNode getActionNode(Action action) {
        RemoveFromDevAction newAction = (RemoveFromDevAction) action;
        ObjectNode actionNode = MAPPER.createObjectNode();
        actionNode.put("from", newAction.getFrom());
        actionNode.put("by", action.getBy());
        actionNode.put("timestamp", action.getTimestamp());
        actionNode.put("action", "REMOVED_FROM_DEV");
        return actionNode;
    }
}
