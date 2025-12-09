package main.command.enums;

import lombok.Getter;

/**
 * Enum representing different command types.
 */
public enum CommandType {
    CREATE_MILESTONE("createMilestone"),
    REPORT_TICKET("reportTicket"),
    VIEW_TICKETS("viewTickets"),
    LOST_INVESTORS("lostInvestors"),
    VIEW_MILESTONES("viewMilestones"),
    ASSIGN_TICKET("assignTicket"),
    VIEW_ASSIGNED_TICKETS("viewAssignedTickets"),
    UNDO_ASSIGN_TICKET("undoAssignTicket"),
    ADD_COMMENT("addComment"),
    UNDO_ADD_COMMENT("undoAddComment");

    @Getter
    public final String name;

    CommandType(String name) {
        this.name = name;
    }

    /**
     * Converts a string to its corresponding CommandType enum value.
     * @param commandType the command type as a string
     * @return the corresponding CommandType enum value, or null if not found
     */
    public static CommandType fromString(String commandType) {
        for (CommandType type : CommandType.values()) {
            if (type.name.equalsIgnoreCase(commandType)) {
                return type;
            }
        }
        return null;
    }
}
