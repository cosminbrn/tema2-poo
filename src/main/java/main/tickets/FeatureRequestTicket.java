package main.tickets;

import main.tickets.enums.BusinessValue;
import main.tickets.enums.CustomerDemand;

public class FeatureRequestTicket extends Ticket {
    private final BusinessValue businessValue;
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
}
