package main.tickets;

import lombok.Getter;
import main.tickets.enums.BusinessPriority;
import main.globals.ExpertiseArea;
import main.tickets.enums.Status;

/**
 * Abstract class representing a ticket with common properties.
 */
@Getter
public abstract class Ticket {
    private final int id;
    private final String type;
    private final BusinessPriority businessPriority;
    private final Status status;
    private final ExpertiseArea expertiseArea;
    private final String reportedBy;

    // Optional fields
    private final String description;

    protected Ticket(Builder<?> builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.businessPriority = builder.businessPriority;
        this.status = builder.status;
        this.expertiseArea = builder.expertiseArea;
        this.reportedBy = builder.reportedBy;
        this.description = builder.description;
    }

    public abstract static class Builder<T extends Builder<T>> {
        private int id;
        private String type;
        private BusinessPriority businessPriority;
        private Status status;
        private ExpertiseArea expertiseArea;
        private String reportedBy;
        private String description = "";

        public T setId(int id) {
            this.id = id;
            return self();
        }

        public T setType(String type) {
            this.type = type;
            return self();
        }

        public T setBusinessPriority(BusinessPriority businessPriority) {
            this.businessPriority = businessPriority;
            return self();
        }

        public T setStatus(Status status) {
            this.status = status;
            return self();
        }

        public T setExpertiseArea(ExpertiseArea expertiseArea) {
            this.expertiseArea = expertiseArea;
            return self();
        }

        public T setReportedBy(String reportedBy) {
            this.reportedBy = reportedBy;
            return self();
        }

        public T setDescription(String description) {
            this.description = description;
            return self();
        }

        protected abstract T self();

        public abstract Ticket build();
    }
}
