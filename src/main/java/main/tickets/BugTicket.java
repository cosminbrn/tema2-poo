package main.tickets;


import lombok.Getter;
import main.tickets.actions.Action;
import main.globals.ticketenums.Frequency;
import main.globals.ticketenums.Severity;

/**
 * Class representing a bug ticket.
 */
public class BugTicket extends Ticket {
    private final String expectedBehavior;
    private final String actualBehavior;
    @Getter
    private final Frequency frequency;
    @Getter
    private final Severity severity;

    // Optional fields
    private final String environment;
    private final int errorCode;
    protected BugTicket(final BugBuilder builder) {
        super(builder);
        this.expectedBehavior = builder.expectedBehavior;
        this.actualBehavior = builder.actualBehavior;
        this.frequency = builder.frequency;
        this.severity = builder.severity;
        this.environment = builder.environment;
        this.errorCode = builder.errorCode;
    }

    public static class BugBuilder extends Ticket.Builder<BugBuilder> {
        private String expectedBehavior;
        private String actualBehavior;
        private Frequency frequency;
        private Severity severity;

        // Optional fields
        private String environment = "";
        private int errorCode = 0;

        /**
         * Set the expected behavior description for the bug.
         * @param expectedBehaviorValue expected behavior text
         * @return builder instance
         */
        public BugBuilder setExpectedBehavior(final String expectedBehaviorValue) {
            this.expectedBehavior = expectedBehaviorValue;
            return this;
        }

        /**
         * Set the actual behavior description for the bug.
         * @param actualBehaviorValue actual behavior text
         * @return builder instance
         */
        public BugBuilder setActualBehavior(final String actualBehaviorValue) {
            this.actualBehavior = actualBehaviorValue;
            return this;
        }

        /**
         * Set the bug frequency.
         * @param frequencyValue frequency value
         * @return builder instance
         */
        public BugBuilder setFrequency(final Frequency frequencyValue) {
            this.frequency = frequencyValue;
            return this;
        }

        /**
         * Set the bug severity.
         * @param severityValue severity value
         * @return builder instance
         */
        public BugBuilder setSeverity(final Severity severityValue) {
            this.severity = severityValue;
            return this;
        }

        /**
         * Set the environment details where the bug occurs.
         * @param environmentValue environment description
         * @return builder instance
         */
        public BugBuilder setEnvironment(final String environmentValue) {
            this.environment = environmentValue;
            return this;
        }

        /**
         * Set the error code associated with the bug.
         * @param errorCodeValue error code value
         * @return builder instance
         */
        public BugBuilder setErrorCode(final int errorCodeValue) {
            this.errorCode = errorCodeValue;
            return this;
        }

        /**
         * Returns this concrete builder instance.
         * @return the current class
         */
        @Override
        protected BugBuilder self() {
            return this;
        }

        /**
         * Build a bug ticket from the accumulated values.
         *
         * @return new bug ticket instance
         */
        @Override
        public BugTicket build() {
            return new BugTicket(this);
        }
    }

    /**
     * Create a deep copy of this bug ticket, including comments and actions.
     *
     * @return deep copy of this bug ticket
     */
    @Override
    public Ticket deepCopy() {
        BugBuilder b = new BugBuilder();
        BugTicket copy = b.setId(getId()).setType(getType())
                .setTitle(getTitle())
                .setBusinessPriority(getBusinessPriority())
                .setStatus(getStatus())
                .setCreatedAt(getCreatedAt())
                .setAssignedAt(getAssignedAt())
                .setSolvedAt(getSolvedAt())
                .setAssignedTo(getAssignedTo())
                .setReportedBy(getReportedBy())
                .setDescription(getDescription() == null ? "" : getDescription())
                .setExpectedBehavior(this.expectedBehavior)
                .setActualBehavior(this.actualBehavior)
                .setFrequency(this.frequency)
                .setSeverity(this.severity)
                .setEnvironment(this.environment)
                .setErrorCode(this.errorCode).build();

        if (getComments() != null) {
            for (Comment c : getComments()) {
                copy.addComment(c.author(), c.comment(), c.timestamp());
            }
        }

        if (getActions() != null) {
            for (Action a : getActions()) {
                copy.getActions().add(a.deepCopy());
            }
        }

        return copy;
    }
}
