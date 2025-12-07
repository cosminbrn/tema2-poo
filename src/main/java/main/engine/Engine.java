package main.engine;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import main.command.Command;
import main.command.CommandFactory;
import main.database.Database;
import main.fileio.CommandInput;
import main.fileio.InputLoader;
import main.globals.WorkflowStage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static main.globals.WorkflowStage.*;

public class Engine {
    private static Engine instance = null;

    @Getter
    private static WorkflowStage currentStage;
    private static String currentStageStartDate;

    private Engine() {
        // Private constructor to prevent instantiation
    }

    public static Engine getInstance() {
        if (instance == null) {
            instance = new Engine();
            instance.currentStage = DONE;
        }
        return instance;
    }

    /**
     * TODO:
     */
    public static void reset() {
        Database.getInstance().reset();
        instance = null;
    }

    /**
     * TODO:
     * @param input
     * @param output
     */
    public static void run(InputLoader input, ArrayNode output) {
        Database db = Database.getInstance();
        db.loadUsers(input.getUserInputs());
        for (CommandInput commandInput : input.getCommandInputs()) {
            updateStage(commandInput);
            Command command = CommandFactory.createCommand(commandInput, output);
            command.execute(commandInput, output);
        }
    }

    /**
     * Method to update the current workflow stage based on the command input timestamp.
     * @param commandInput The command input containing the timestamp.
     */
    private static void updateStage(CommandInput commandInput) {
        if (currentStage == DONE) {
            currentStageStartDate = commandInput.getTimestamp();
        } else if (currentStage == TESTING) {
            LocalDate now = LocalDate.parse(currentStageStartDate);
            LocalDate due = LocalDate.parse(commandInput.getTimestamp());

            int daysBetween = (int) ChronoUnit.DAYS.between(now, due) + 1;
            if (daysBetween > TESTING_STAGE_DURATION.getDefaultDuration()) {
                currentStage = DEVELOPMENT;
            }
        }
    }
}
