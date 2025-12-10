package main.command;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.fileio.CommandInput;

import static main.App.MAPPER;

/**
 * Base command class: provides a convenience JSON node and a helper to append
 * error outputs to the shared result array. Subclasses must implement
 * {@link #execute(CommandInput, ArrayNode)}.
 */
public abstract class Command {
    protected ObjectNode node = MAPPER.createObjectNode();

    public abstract void execute(CommandInput commandInput, ArrayNode output);

    /**
     * Append an error object for the provided input to the output array.
     *
     * @param input        the original command input
     * @param output       the array to append the error node to
     * @param errorMessage the error message to include
     */
    public void addErrorOutput(final CommandInput input, final ArrayNode output,
                               final String errorMessage) {
        node.put("command", input.getCommand());
        node.put("username", input.getUsername());
        node.put("timestamp", input.getTimestamp());
        node.put("error", errorMessage);
        output.add(node);
    }
}
