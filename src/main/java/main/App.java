package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import main.engine.Engine;
import main.fileio.InputLoader;

import java.io.File;
import java.io.IOException;

/**
 * main.App represents the main application logic that processes input commands,
 * generates outputs, and writes them to a file
 */
public final class App {
    private App() { }

    private static final String inputUserFile = "input/database/users.json";
    public static final ObjectMapper MAPPER = new ObjectMapper();

    private static final ObjectWriter writer = MAPPER.writer().withDefaultPrettyPrinter();

    /**
     * Runs the application: reads commands from an input file,
     * processes them, generates results, and writes them to an output file
     *
     * @param inputPath path to the input file containing commands
     * @param outputPath path to the file where results should be written
     */
    public static void run(final String inputPath, final String outputPath)
            throws IOException {
        final ArrayNode outputs = MAPPER.createArrayNode();

        final InputLoader inputLoader = new InputLoader(inputPath, inputUserFile);

        Engine.reset();
        Engine.init();
        Engine.run(inputLoader, outputs);


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