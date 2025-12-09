package main.tickets.enums;

import lombok.Getter;

public enum Status {
    OPEN("OPEN"),
    IN_PROGRESS("IN_PROGRESS"),
    RESOLVED("RESOLVED"),
    CLOSED("CLOSED");

    @Getter
    public final String statusName;

    Status(String statusName) {
        this.statusName = statusName;
    }

    public static Status fromString(String statusStr) {
        for (Status status : Status.values()) {
            if (status.statusName.equalsIgnoreCase(statusStr)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for status: " + statusStr);
    }
}
