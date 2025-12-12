package main.fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Basic skeleton for loading input JSON file as a Map.
 * Students should implement deeper parsing themselves.
 */
@Getter
public final class InputLoader {
    private final ArrayList<UserInput> userInputs;
    private final ArrayList<CommandInput> commandInputs;

    public InputLoader(final String filePath, final String usersPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode commandRootNode = mapper.readTree(new File(filePath));
        if (commandRootNode.isArray()) {
            CommandInput[] arr = mapper.treeToValue(commandRootNode, CommandInput[].class);
            this.commandInputs = new ArrayList<>(Arrays.asList(arr));
        } else {
            CommandRoot commandRoot = mapper.treeToValue(commandRootNode, CommandRoot.class);
            this.commandInputs = new ArrayList<>(commandRoot.commands);
        }

        JsonNode userRootNode = mapper.readTree(new File(usersPath));
        if (userRootNode.isArray()) {
            UserInput[] arr = mapper.treeToValue(userRootNode, UserInput[].class);
            this.userInputs = new ArrayList<>(Arrays.asList(arr));
        } else {
            UserRoot userRoot = mapper.treeToValue(userRootNode, UserRoot.class);
            this.userInputs = new ArrayList<>(userRoot.users);
        }
    }

    @Data
    @NoArgsConstructor
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static final class UserRoot {
        private List<UserInput> users;
    }

    @Data
    @NoArgsConstructor
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static final class CommandRoot {
        private List<CommandInput> commands;
    }

}
