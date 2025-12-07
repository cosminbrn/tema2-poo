package main.engine;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.command.Command;
import main.command.CommandFactory;
import main.database.Database;
import main.fileio.CommandInput;
import main.fileio.InputLoader;
import main.globals.WorkflowStage;

import static main.globals.WorkflowStage.DONE;

public class Engine {
    private static Engine instance = null;

    private WorkflowStage currentStage;

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
            Command command = CommandFactory.createCommand(commandInput, output);
            command.execute(commandInput, output);
        }
    }
}
