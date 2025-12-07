package main.fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CommandInput {
    private String command;
    private String username;
    private String timestamp;
    private String name;
    private String dueDate;
    private String comment;
    private String[] blockingFor;
    private String[] assignedDevs;
    private int[] tickets;
    private int ticketID;
    private FiltersInput filters;
    private ParamsInput params;
}

