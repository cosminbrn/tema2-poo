package main.engine;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
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


    @Getter @Setter
    private static WorkflowStage currentStage;
    private static LocalDate currentStageStartDate;
    private static LocalDate currentDay;
    private static int commandInputIndex;

    private Engine() {
        // Private constructor to prevent instantiation
    }

    public static Engine getInstance() {
        if (instance == null) {
            instance = new Engine();
            currentStage = DONE;
            commandInputIndex = 0;
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

    public static void init() {
        getInstance();
        Database.init();
    }


    /**
     * Method to run the engine with the given input and output. It runs on a day-by-day basis, updating the database every iteration.
     * @param input The input loader containing the command inputs.
     * @param output The output array node to store the results.
     */
    public static void run(InputLoader input, ArrayNode output) {
        Database db = Database.getInstance();
        db.loadUsers(input.getUserInputs());
        currentStage = TESTING;
        CommandInput commandInput = getNextCommandInput(input);
        currentDay = LocalDate.parse(commandInput.getTimestamp());
        currentStageStartDate = currentDay;
        while (currentStage != BANKRUPT && currentDay.isBefore(LocalDate.parse("2027-01-01"))) {
            updateStage();
            db.updateDatabase(currentDay);
            while (LocalDate.parse(commandInput.getTimestamp()).isEqual(currentDay)) {
                Command command = CommandFactory.createCommand(commandInput, output);
                command.execute(commandInput, output);
                if (commandInputIndex < input.getCommandInputs().size()) {
                    commandInput = getNextCommandInput(input);
                } else {
                    currentStage = BANKRUPT;
                    break;
                }
            }
            currentDay = currentDay.plusDays(1);
        }
    }

    /**
     * Method to get the next command input from the database.
     * @return The next command input.
     */
    private static CommandInput getNextCommandInput(InputLoader input) {
        CommandInput commandInput = input.getCommandInputs().get(commandInputIndex);
        commandInputIndex++;
        return commandInput;
    }

    /**
     * Method to update the current workflow stage based on the command input timestamp.
     */
    private static void updateStage() {
        if (currentStage == TESTING) {
            if (currentStageStartDate == null) {
                currentStageStartDate = currentDay;
                return;
            }
            long daysBetween = ChronoUnit.DAYS.between(currentStageStartDate, currentDay) + 1;
            if (daysBetween > TESTING_STAGE_DURATION.getDefaultDuration()) {
                currentStage = DEVELOPMENT;
                currentStageStartDate = currentDay;
            }
        }
    }

    public void setCurrentState(WorkflowStage workflowStage) {
        currentStage = workflowStage;
    }
}
