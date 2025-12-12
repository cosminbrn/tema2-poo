package main.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.database.Database;
import main.engine.Engine;
import main.fileio.CommandInput;
import main.globals.WorkflowStage;
import main.globals.commandenums.ErrorMessages;
import main.globals.milestoneenums.MilestoneState;
import main.milestones.Milestone;

import java.util.List;

public class StartTestingPhaseCommand extends Command {
    @Override
    public void execute(CommandInput commandInput, ArrayNode output) {
        Database db = Database.getInstance();

        List<Milestone> milestones = db.getMilestones();
        for (Milestone milestone : milestones) {
            if (milestone.getStatus() == MilestoneState.ACTIVE) {
                addErrorOutput(commandInput, output, ErrorMessages.CANNOT_START_TESTING.getErrorMessage());
            }
        }

        Engine.setCurrentStage(WorkflowStage.TESTING);
        Engine.setCurrentStageStartDate(null);
    }
}
