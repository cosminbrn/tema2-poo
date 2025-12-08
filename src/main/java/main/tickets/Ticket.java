package main.tickets;

import lombok.Getter;
import lombok.Setter;
import main.globals.TicketType;
import main.tickets.enums.BusinessPriority;
import main.globals.ExpertiseArea;
import main.tickets.enums.Status;

import static main.tickets.enums.BusinessPriority.*;

/**
 * Abstract class representing a ticket with common properties.
 */
@Getter
public abstract class Ticket {
    private final int id;
    private final TicketType type;
    private final String title;
    @Setter
    private BusinessPriority businessPriority;
    private final Status status;
    private final String createdAt;
    private final String assignedAt;
    private final String reportedBy;
    private final String solvedAt;
    private final String assignedTo;
    private final String[] comments;
    private final ExpertiseArea expertiseArea;

    // Optional fields
    private final String description;

    // Additional fields
    @Setter @Getter
    private String assignedMilestone = "";

    protected Ticket(Builder<?> builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.title = builder.title;
        this.businessPriority = builder.businessPriority;
        this.status = builder.status;
        this.createdAt = builder.createdAt;
        this.assignedAt = builder.assignedAt;
        this.solvedAt = builder.solvedAt;
        this.assignedTo = builder.assignedTo;
        this.reportedBy = builder.reportedBy;
        this.comments = builder.comments;
        this.expertiseArea = builder.expertiseArea;
        this.description = builder.description;
    }

    public abstract static class Builder<T extends Builder<T>> {
        private int id;
        private TicketType type;
        private String title;
        private BusinessPriority businessPriority;
        private Status status;
        private ExpertiseArea expertiseArea;
        private String reportedBy;
        private String description = "";
        private String createdAt;
        private String assignedTo = "";
        private String assignedAt = "";
        private String solvedAt = "";
        private String[] comments = new String[0];

        public T setAssignedAt(String assignedAt) {
            this.assignedAt = assignedAt;
            return self();
        }

        public T setSolvedAt(String solvedAt) {
            this.solvedAt = solvedAt;
            return self();
        }

        public T setComments(String[] comments) {
            this.comments = comments;
            return self();
        }

        public T setId(int id) {
            this.id = id;
            return self();
        }

        public T setAssignedTo(String assignedTo) {
            this.assignedTo = assignedTo;
            return self();
        }

        public T setType(TicketType type) {
            this.type = type;
            return self();
        }

        public T setTitle(String title) {
            this.title = title;
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

        public T setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
            return self();
        }

        protected abstract T self();

        public abstract Ticket build();
    }

    public void updatePriority() {
        if (this.businessPriority == LOW) {
            this.businessPriority = MEDIUM;
        } else if (this.businessPriority == MEDIUM) {
            this.businessPriority = HIGH;
        } else if (this.businessPriority == HIGH) {
            this.businessPriority = CRITICAL;
        }
    }
}
