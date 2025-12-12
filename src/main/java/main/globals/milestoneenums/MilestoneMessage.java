package main.globals.milestoneenums;

import lombok.Getter;

/**
 * Enum representing milestone-related messages.
 */
public enum MilestoneMessage {
    MILESTONE_CREATION("New milestone %s has been created with due date %s."),
    MILESTONE_OPENED("Milestone %s is now unblocked as ticket %d has been CLOSED."),
    MILESTONE_ALMOST_DUE("Milestone %s is due tomorrow. All unresolved tickets are now CRITICAL."),
    MILESTONE_UNLOCKED_OVERDUE("Milestone %s was unblocked after due date. "
            + "All active tickets are now CRITICAL.");

    @Getter
    private final String message;

    MilestoneMessage(final String message) {
        this.message = message;
    }
}
