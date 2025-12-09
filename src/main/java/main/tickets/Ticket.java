package main.tickets;

import lombok.Getter;
import lombok.Setter;
import main.globals.TicketType;
import main.tickets.actions.*;
import main.tickets.enums.ActionType;
import main.tickets.enums.BusinessPriority;
import main.globals.ExpertiseArea;
import main.tickets.enums.Status;

import java.util.ArrayList;
import java.util.List;

import static main.tickets.enums.ActionType.*;
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
    @Setter
    private Status status;
    private final String createdAt;
    @Setter
    private String assignedAt;
    private final String reportedBy;
    @Setter
    private String solvedAt;
    @Setter
    private String assignedTo;
    private final List<Comment> comments;
    @Getter
    private final List<Action> actions;
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
        this.actions = builder.actions;
        this.expertiseArea = builder.expertiseArea;
        this.description = builder.description;
    }

    public record Comment(String author, String comment, String timestamp) {

    }

    public static class ActionFactory {
        /**
         * Static Factory Method to Create Actions based on their type.
         * @param type The type of action to create.
         * @param args The String args. They go as follows:
         *             args[0] = by
         *             args[1] = timestamp
         *             args[2] = from
         *             args[3] = to
         *             args[4] = milestone
         * @return The created Action object.
         */
        public static Action createAction(ActionType type, String... args) {
            return switch(type) {
                case ASSIGNED -> new AssignAction(args[0], args[1]);
                case DE_ASSIGNED -> new DeassignAction(args[0], args[1]);
                case STATUS_CHANGED -> new StatusChangeAction(args[0], args[1], Status.fromString(args[2]), Status.fromString(args[3]));
                case ADDED_TO_MILESTONE -> new AddToMilestoneAction(args[0], args[1], args[4]);
                case REMOVED_FROM_DEV -> new RemoveFromDevAction(args[0], args[1], args[2]);
            };
        }
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
        private List<Comment> comments = new ArrayList<>();
        private List<Action> actions = new ArrayList<>();

        public T setAssignedAt(String assignedAt) {
            this.assignedAt = assignedAt;
            return self();
        }

        public T setSolvedAt(String solvedAt) {
            this.solvedAt = solvedAt;
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

    public void addComment(String username, String comment, String timestamp) {
        this.comments.add(new Comment(username, comment, timestamp));
    }

    public List<Comment> getCommentsByUser(String username) {
        List<Comment> userComments = new ArrayList<>();
        for (Comment comment : this.comments) {
            if (comment.author().equalsIgnoreCase(username)) {
                userComments.add(comment);
            }
        }
        return userComments;
    }

    public void removeLastCommentByUser(String username) {
        for (int i = comments.size() - 1; i >= 0; i--) {
            if (comments.get(i).author().equalsIgnoreCase(username)) {
                comments.remove(i);
                break;
            }
        }
    }

    public Status updateStatus() {
        if (this.status == Status.OPEN) {
            this.status = Status.IN_PROGRESS;
        } else if (this.status == Status.IN_PROGRESS) {
            this.status = Status.RESOLVED;
        } else if (this.status == Status.RESOLVED) {
            this.status = Status.CLOSED;
        }
        return this.status;
    }

    public Status undoStatus() {
        if (this.status == Status.CLOSED) {
            this.status = Status.RESOLVED;
        } else if (this.status == Status.RESOLVED) {
            this.status = Status.IN_PROGRESS;
        }
        return this.status;
    }

    private <T> void executeAddAction(ActionType actionType, String by, String timestamp,
                              T from, Status to, String milestone) {

        String fromStr = from != null ? from.toString() : "";
        String toStr = to != null ? to.toString() : "";

        Action action = ActionFactory.createAction(actionType, by, timestamp, fromStr, toStr, milestone);
        this.actions.add(action);
    }

    public void addAction(ActionType actionType, String by, String timestamp) {
        executeAddAction(actionType, by, timestamp, null, null, "");
    }

    public void addAction(ActionType actionType, String by, String timestamp, Status from, Status to) {
        executeAddAction(actionType, by, timestamp, from, to, "");
    }

    public void addAction(ActionType actionType, String by, String timestamp, String arg) {
        if (actionType == REMOVED_FROM_DEV) {
            executeAddAction(actionType, by, timestamp, arg, null, "");
        } else if (actionType == ADDED_TO_MILESTONE) {
            executeAddAction(actionType, by, timestamp, null, null, arg);
        } else {
            throw new IllegalArgumentException(actionType.toString());
        }
    }

    /**
     * Creates a deep copy of the ticket.
     * @return A new Ticket object that is a deep copy of the current ticket.
     */
    public abstract Ticket deepCopy();
}

