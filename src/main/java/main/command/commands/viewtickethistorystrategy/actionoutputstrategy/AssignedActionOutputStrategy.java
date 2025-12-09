package main.command.commands.viewtickethistorystrategy.actionoutputstrategy;

import com.fasterxml.jackson.databind.node.ObjectNode;
import main.tickets.actions.Action;

import static main.App.MAPPER;

public class AssignedActionOutputStrategy implements ActionOutputStrategy {

    @Override
    public ObjectNode getActionNode(Action action) {
        ObjectNode actionNode = MAPPER.createObjectNode();
        actionNode.put("by", action.getBy());
        actionNode.put("timestamp", action.getTimestamp());
        actionNode.put("action", "ASSIGNED");
        return actionNode;
    }
}
