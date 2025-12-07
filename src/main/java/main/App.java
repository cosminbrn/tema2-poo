package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.fileio.InputLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * main.App represents the main application logic that processes input commands,
 * generates outputs, and writes them to a file
 */
public class App {
    private static final String inputUserFile = "input/database/users.json";
    public static final ObjectMapper MAPPER = new ObjectMapper();

    private static final ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

    /**
     * Runs the application: reads commands from an input file,
     * processes them, generates results, and writes them to an output file
     *
     * @param inputPath path to the input file containing commands
     * @param outputPath path to the file where results should be written
     */
    public static void run(String inputPath, String outputPath) throws IOException {
        // feel free to change this if needed (however keep 'outputs' variable name to be used for writing)
        ArrayNode outputs = MAPPER.createArrayNode();

        /*
            TODO 1 :
            Load initial user data and commands. we strongly recommend using jackson library.
            you can use the reading from hw1 as a reference.
            however you can use some of the more advanced features of
            jackson library, available here: https://www.baeldung.com/jackson-annotations
        */

        InputLoader inputLoader = new InputLoader(inputPath, inputUserFile);

        // TODO 2: process commands.

        // TODO 3: create objectnodes for output, add them to outputs list.

        // --- Begin: test helper to dump loaded nodes into outputs ---
        // Create an ObjectNode that contains both users and commands as arrays
        ObjectNode dumpNode = MAPPER.createObjectNode();
        // Convert the loaded users and commands into JSON tree nodes and attach
        dumpNode.set("users", MAPPER.valueToTree(inputLoader.getUsers()));
        dumpNode.set("commands", MAPPER.valueToTree(inputLoader.getCommands()));
        // Add this node to the outputs array so it will be written to outputPath
        outputs.add(dumpNode);
        // --- End: test helper ---


        // DO NOT CHANGE THIS SECTION IN ANY WAY
        try {
            File outputFile = new File(outputPath);
            outputFile.getParentFile().mkdirs();
            writer.withDefaultPrettyPrinter().writeValue(outputFile, outputs);
        } catch (IOException e) {
            System.out.println("error writing to output file: " + e.getMessage());
        }
    }
}
