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
    /**
     * Executes the command that starts the testing phase if no milestones
     * are currently active; otherwise emits an error.
     * @param commandInput the parsed command input
     * @param output       the JSON array where results are appended
     */
    @Override
    public void execute(final CommandInput commandInput,
                        final ArrayNode output) {
        List<Milestone> milestones = db.getMilestones();
        for (Milestone milestone : milestones) {
            if (milestone.getStatus() == MilestoneState.ACTIVE) {
                addErrorOutput(commandInput, output,
                        ErrorMessages.CANNOT_START_TESTING.getErrorMessage());
                return;
            }
        }

        Engine.setCurrentStage(WorkflowStage.TESTING);
        Engine.setCurrentStageStartDate(null);
    }
}
