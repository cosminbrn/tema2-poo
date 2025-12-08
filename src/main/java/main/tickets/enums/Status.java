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
}
