package main.command.enums;

import lombok.Getter;

public enum ErrorMessages {
    REPORT_ONLY_DURING_TESTING("Tickets can only be reported during testing phases."),
    ANONYMOUS_REPORTING_ONLY_FOR_BUGS("Anonymous reports are only allowed for tickets of type BUG."),
    USER_NOT_FOUND("The user %s does not exist."),
    TICKET_ALREADY_ASSIGNED_TO_MILESTONE("Tickets %d already assigned to milestone %s."),
    REQUIRED_ROLE_MANAGER("The user does not have permission to execute this command: required role MANAGER; user role %s.");

    @Getter
    private final String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
