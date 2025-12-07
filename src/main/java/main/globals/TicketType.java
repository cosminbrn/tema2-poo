package main.globals;

/**
 * Enum representing different types of tickets.
 */
public enum TicketType {
    BUG,
    FEATURE_REQUEST,
    UI_FEEDBACK;

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
}
