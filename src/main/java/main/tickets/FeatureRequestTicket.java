package main.tickets;

import lombok.Getter;
import main.tickets.actions.Action;
import main.globals.ticketenums.BusinessValue;
import main.globals.ticketenums.CustomerDemand;

public class FeatureRequestTicket extends Ticket {
    @Getter
    private final BusinessValue businessValue;
    @Getter
    private final CustomerDemand customerDemand;
    protected FeatureRequestTicket(final FeatureRequestBuilder builder) {
        super(builder);
        this.businessValue = builder.businessValue;
        this.customerDemand = builder.customerDemand;
    }

    public static class FeatureRequestBuilder extends Ticket.Builder<FeatureRequestBuilder> {
        private BusinessValue businessValue;
        private CustomerDemand customerDemand;

        /**
         * Set the business value of the feature request.
         * @param businessValue business value
         * @return builder instance
         */
        public FeatureRequestBuilder setBusinessValue(final BusinessValue businessValue) {
            this.businessValue = businessValue;
            return this;
        }

        /**
         * Set the customer demand of the feature request.
         * @param customerDemand customer demand
         * @return builder instance
         */
        public FeatureRequestBuilder setCustomerDemand(final CustomerDemand customerDemand) {
            this.customerDemand = customerDemand;
            return this;
        }

        /**
         * Return the concrete builder instance.
         * @return builder instance
         */
        @Override
        protected FeatureRequestBuilder self() {
            return this;
        }

        /**
         * Build a FeatureRequestTicket from the accumulated values.
         * @return new FeatureRequestTicket
         */
        @Override
        public FeatureRequestTicket build() {
            return new FeatureRequestTicket(this);
        }
    }

    /**
     * Create a deep copy of this feature request ticket.
     * @return deep copy of the ticket
     */
    @Override
    public Ticket deepCopy() {
        FeatureRequestBuilder b = new FeatureRequestBuilder();
        FeatureRequestTicket copy = b.setId(getId()).setType(getType())
                .setTitle(getTitle())
                .setBusinessPriority(getBusinessPriority())
                .setStatus(getStatus())
                .setCreatedAt(getCreatedAt())
                .setAssignedAt(getAssignedAt())
                .setSolvedAt(getSolvedAt())
                .setAssignedTo(getAssignedTo())
                .setReportedBy(getReportedBy())
                .setDescription(getDescription())
                .setBusinessValue(this.businessValue)
                .setCustomerDemand(this.customerDemand).build();

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
