package main.tickets;

import lombok.Getter;
import main.tickets.actions.Action;
import main.tickets.enums.BusinessValue;
import main.tickets.enums.CustomerDemand;

public class FeatureRequestTicket extends Ticket {
    @Getter
    private final BusinessValue businessValue;
    @Getter
    private final CustomerDemand customerDemand;

    protected FeatureRequestTicket(FeatureRequestBuilder builder) {
        super(builder);
        this.businessValue = builder.businessValue;
        this.customerDemand = builder.customerDemand;
    }

    public static class FeatureRequestBuilder extends Ticket.Builder<FeatureRequestBuilder> {
        private BusinessValue businessValue;
        private CustomerDemand customerDemand;

        public FeatureRequestBuilder setBusinessValue(BusinessValue businessValue) {
            this.businessValue = businessValue;
            return this;
        }

        public FeatureRequestBuilder setCustomerDemand(CustomerDemand customerDemand) {
            this.customerDemand = customerDemand;
            return this;
        }

        @Override
        protected FeatureRequestBuilder self() {
            return this;
        }

        @Override
        public FeatureRequestTicket build() {
            return new FeatureRequestTicket(this);
        }
    }

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
