package main.command;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import main.command.enums.ErrorMessages;
import main.fileio.CommandInput;

import static main.App.MAPPER;

public abstract class Command {
    protected ObjectNode node = MAPPER.createObjectNode();

    public abstract void execute(CommandInput commandInput, ArrayNode output);

    public void addErrorOutput(final CommandInput input, final ArrayNode output, final String errorMessage) {
        node.put("command", input.getCommand());
        node.put("username", input.getUsername());
        node.put("timestamp", input.getTimestamp());
        node.put("error", errorMessage);
        output.add(node);
    }
}
