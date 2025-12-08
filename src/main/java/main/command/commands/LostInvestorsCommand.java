package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.engine.Engine;
import main.fileio.CommandInput;

import static main.globals.WorkflowStage.BANKRUPT;

public class LostInvestorsCommand extends Command {
    @Override
    public void execute(CommandInput commandInput, ArrayNode output) {
        Engine engine = Engine.getInstance();
        engine.setCurrentState(BANKRUPT);
    }
}
