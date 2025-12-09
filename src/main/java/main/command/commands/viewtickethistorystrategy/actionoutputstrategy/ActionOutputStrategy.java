package main.command.commands.viewtickethistorystrategy.actionoutputstrategy;

import com.fasterxml.jackson.databind.node.ObjectNode;
import main.tickets.actions.Action;

public interface ActionOutputStrategy {
   ObjectNode getActionNode(Action action);
}
