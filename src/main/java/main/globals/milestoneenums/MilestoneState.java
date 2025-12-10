package main.globals.milestoneenums;

import lombok.Getter;

/**
 * Enum representing the state of a milestone.
 */
public enum MilestoneState {
    ACTIVE("ACTIVE"),
    CLOSED("CLOSED"),
    COMPLETED("COMPLETED");

    @Getter
    private final String state;

    MilestoneState(final String state) {
        this.state = state;
    }
}
