package main.tickets;


import main.tickets.enums.Frequency;
import main.tickets.enums.Severity;

/**
 * Class representing a bug ticket.
 */
public class BugTicket extends Ticket {
    private final String expectedBehavior;
    private final String actualBehavior;
    private final Frequency frequency;
    private final Severity severity;

    // Optional fields
    private final String environment;
    private final int errorCode;

    protected BugTicket(BugBuilder builder) {
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

        public BugBuilder setExpectedBehavior(String expectedBehavior) {
            this.expectedBehavior = expectedBehavior;
            return this;
        }

        public BugBuilder setActualBehavior(String actualBehavior) {
            this.actualBehavior = actualBehavior;
            return this;
        }

        public BugBuilder setFrequency(Frequency frequency) {
            this.frequency = frequency;
            return this;
        }

        public BugBuilder setSeverity(Severity severity) {
            this.severity = severity;
            return this;
        }

        public BugBuilder setEnvironment(String environment) {
            this.environment = environment;
            return this;
        }

        public BugBuilder setErrorCode(int errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        @Override
        protected BugBuilder self() {
            return this;
        }

        @Override
        public BugTicket build() {
            return new BugTicket(this);
        }
    }
}
