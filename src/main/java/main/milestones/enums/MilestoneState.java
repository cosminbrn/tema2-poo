package main.milestones.enums;

import lombok.Getter;

public enum MilestoneState {
    ACTIVE("ACTIVE"),
    CLOSED("CLOSED"),
    COMPLETED("COMPLETED");

    @Getter
    private final String state;

    MilestoneState(String state) {
        this.state = state;
    }
}
