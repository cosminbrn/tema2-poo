package main.globals.commandenums;

/**
 * Enum holding formatted error messages used across commands.
 */
public enum ErrorMessages {
    REPORT_ONLY_DURING_TESTING("Tickets can only be reported during testing phases."),
    ANONYMOUS_REPORTING_ONLY_FOR_BUGS("Anonymous reports are only "
            + "allowed for tickets of type BUG."),
    USER_NOT_FOUND("The user %s does not exist."),
    TICKET_ALREADY_ASSIGNED_TO_MILESTONE("Tickets %d already assigned to milestone %s."),
    REQUIRED_ROLE_MANAGER("The user does not have permission to execute "
            + "this command: required role MANAGER; user role %s."),
    REQUIRED_ROLE_DEVELOPER("The user does not have permission to execute "
            + "this command: required role DEVELOPER; user role %s."),
    DEVELOPER_LACKS_EXPERTISE("Developer %s cannot assign ticket %d due "
            + "to expertise area. Required: %s; Current: %s."),
    DEVELOPER_LACKS_SENIORITY("Developer %s cannot assign ticket %d due "
            + "to seniority level. Required: %s; Current: %s."),
    TICKET_NOT_OPEN("Only OPEN tickets can be assigned."),
    DEVELOPER_NOT_ASSIGNED_TO_MILESTONE("Developer %s is not assigned to milestone %s."),
    MILESTONE_BLOCKED("Cannot assign ticket %d from blocked milestone %s."),
    TICKET_NOT_IN_PROGRESS("Only IN_PROGRESS tickets can be unassigned."),
    TICKET_NOT_ASSIGNED_TO_DEVELOPER("Ticket %d is not assigned to the developer %s."),
    TICKET_NOT_ASSIGNED_TO_DEVELOPER_WITHOUT_THE("Ticket %d is not assigned to developer %s."),
    COMMENTS_NOT_ALLOWED_ON_ANONYMOUS_TICKETS("Comments are not allowed on anonymous tickets."),
    REPORTER_TICKET_IS_CLOSED("Reporters cannot comment on CLOSED tickets."),
    NOT_10_CHARACTERS_LONG("Comment must be at least 10 characters long."),
    TICKET_NOT_REPORTED_BY_REPORTER("Reporter %s cannot comment on ticket %d."),
    CANNOT_START_TESTING("Cannot start a new testing phase."),
    REPORTERS_NOT_ALLOWED("The user does not have permission to execute "
            + "this command: required role DEVELOPER, MANAGER; user role REPORTER.");

    private final String errorMessage;

    ErrorMessages(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
