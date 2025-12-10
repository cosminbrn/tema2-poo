package main.command.commands.view.history.viewtickethistorystrategy.actionoutputstrategy;

import com.fasterxml.jackson.databind.node.ObjectNode;
import main.tickets.actions.Action;

/**
 * Strategy interface to convert an Action into a JSON node for output.
 */
public interface ActionOutputStrategy {
   /**
    * Convert the provided action into an ObjectNode for output.
    * @param action the action to convert
    * @return JSON node representing the action
    */
   ObjectNode getActionNode(Action action);
}
