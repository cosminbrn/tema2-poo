package main.command.enums;

import lombok.Getter;

public enum ErrorMessages {
    REPORT_ONLY_DURING_TESTING("Tickets can only be reported during testing phases."),
    ANONYMOUS_REPORTING_ONLY_FOR_BUGS("Anonymous reporting is only allowed for bug tickets.");

    @Getter
    private final String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
