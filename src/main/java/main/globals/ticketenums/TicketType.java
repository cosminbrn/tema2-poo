package main.globals.ticketenums;

import lombok.Getter;

/**
 * Enum representing different types of tickets.
 */
public enum TicketType {
    BUG("BUG"),
    FEATURE_REQUEST("FEATURE_REQUEST"),
    UI_FEEDBACK("UI_FEEDBACK"),;

    @Getter
    private final String typeName;

    TicketType(final String typeName) {
        this.typeName = typeName;
    }

    /**
     * Gets the TicketType enum constant by its name, ignoring case.
     * @param name the name of the ticket type
     * @return the corresponding TicketType, or null if not found
     */
    public static TicketType fromString(final String name) {
        for (TicketType type : TicketType.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Converts a TicketType enum constant to its string representation.
     * @param type the TicketType to convert
     * @return the name of the TicketType
     */
    public static String toString(final TicketType type) {
        return type.name();
    }

}
