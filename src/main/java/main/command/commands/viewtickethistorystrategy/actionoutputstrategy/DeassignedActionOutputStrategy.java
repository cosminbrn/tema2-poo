package main.command.commands.viewtickethistorystrategy.actionoutputstrategy;

import com.fasterxml.jackson.databind.node.ObjectNode;
import main.tickets.actions.Action;

import static main.App.MAPPER;

public class DeassignedActionOutputStrategy implements ActionOutputStrategy{

    @Override
    public ObjectNode getActionNode(Action action) {
        ObjectNode actionNode = MAPPER.createObjectNode();
        actionNode.put("by", action.getBy());
        actionNode.put("timestamp", action.getTimestamp());
        actionNode.put("action", "DE-ASSIGNED");
        return actionNode;
    }
}
