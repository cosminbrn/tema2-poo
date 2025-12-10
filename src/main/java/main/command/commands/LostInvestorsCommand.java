package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.engine.Engine;
import main.fileio.CommandInput;

import static main.globals.WorkflowStage.BANKRUPT;

/**
 * Command to mark the application as bankrupt (lost investors).
 */
public class LostInvestorsCommand extends Command {
    /**
     * Execute the lost investors command and set workflow state to BANKRUPT.
     * @param commandInput parsed command input
     * @param output JSON array to append results to
     */
    @Override
    public void execute(CommandInput commandInput, ArrayNode output) {
        Engine engine = Engine.getInstance();
        engine.setCurrentState(BANKRUPT);
    }
}
