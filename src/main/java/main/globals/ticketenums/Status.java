package main.globals.ticketenums;

import lombok.Getter;

public enum Status {
    OPEN("OPEN"),
    IN_PROGRESS("IN_PROGRESS"),
    RESOLVED("RESOLVED"),
    CLOSED("CLOSED");

    @Getter
    private final String statusName;

    Status(final String statusName) {
        this.statusName = statusName;
    }

    /**
     * Converts a string to its corresponding Status enum constant.
     * @param statusStr the string representation of the status
     * @return the corresponding Status enum constant
     * @throws IllegalArgumentException if no matching status is found
     */
    public static Status fromString(final String statusStr) {
        for (Status status : Status.values()) {
            if (status.statusName.equalsIgnoreCase(statusStr)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for status: " + statusStr);
    }
}
