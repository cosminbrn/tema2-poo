package main.globals;

import lombok.Getter;

/**
 * Enum representing the various stages of a workflow.
 */
public enum WorkflowStage {
    TESTING,
    DEVELOPMENT,
    VERIFICATION,
    DONE,
    BANKRUPT,

    TESTING_STAGE_DURATION(12);

    @Getter
    private final int defaultDuration;

    WorkflowStage() {
        this.defaultDuration = -1;
    }

    WorkflowStage(final int defaultDuration) {
        this.defaultDuration = defaultDuration;
    }
}
