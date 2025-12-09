package main.command.commands.viewtickethistorystrategy.actionoutputstrategy;

import com.fasterxml.jackson.databind.node.ObjectNode;
import main.tickets.actions.Action;
import main.tickets.actions.StatusChangeAction;

import static main.App.MAPPER;

public class StatusChangedActionOutputStrategy implements ActionOutputStrategy {
    @Override
    public ObjectNode getActionNode(final Action action) {
        StatusChangeAction newAction = (StatusChangeAction) action;
        ObjectNode actionNode = MAPPER.createObjectNode();
        actionNode.put("from", newAction.getFrom().toString());
        actionNode.put("to", newAction.getTo().toString());
        actionNode.put("by", newAction.getBy());
        actionNode.put("timestamp", newAction.getTimestamp());
        actionNode.put("action", "STATUS_CHANGED");
        return actionNode;
    }
}
