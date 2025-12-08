package main.globals;

import lombok.Getter;

public enum WorkflowStage {
    TESTING,
    DEVELOPMENT,
    VERIFICATION,
    DONE,
    BANKRUPT,

    TESTING_STAGE_DURATION(12);

    @Getter
    public final int defaultDuration;

    WorkflowStage() {
        this.defaultDuration = -1;
    }

    WorkflowStage(int defaultDuration) {
        this.defaultDuration = defaultDuration;
    }


}
