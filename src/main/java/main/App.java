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
 * generates outputs, and writes them to a file.
 */
public class App {
    // path to the initial users database file inside the project input folder
    private static final String INPUT_USER_FILE = "input/database/users.json";

    public static final ObjectMapper MAPPER = new ObjectMapper();

    private static final ObjectWriter WRITER = MAPPER.writer()
            .withDefaultPrettyPrinter();

    // utility class: prevent instantiation
    private App() {
    }

    /**
     * Runs the application: reads commands from an input file, processes them,
     * generates results and writes them to an output file.
     *
     * @param inputPath  path to the input file containing commands
     * @param outputPath path to the file where results should be written
     */
    public static void run(final String inputPath, final String outputPath)
            throws IOException {
        // keep the variable name 'outputs' as the tests rely on it
        ArrayNode outputs = MAPPER.createArrayNode();

        InputLoader inputLoader = new InputLoader(inputPath, INPUT_USER_FILE);

        Engine.reset();
        Engine.init();
        Engine.run(inputLoader, outputs);

        // DO NOT CHANGE THIS SECTION IN ANY WAY
        try {
            File outputFile = new File(outputPath);
            File parent = outputFile.getParentFile();
            if (parent != null && !parent.exists()) {
                boolean created = parent.mkdirs();
                if (!created) {
                    System.out.println("warning: could not create parent directories for output file");
                }
            }
            WRITER.writeValue(outputFile, outputs);
        } catch (IOException e) {
            System.out.println("error writing to output file: " + e.getMessage());
        }
    }
}
