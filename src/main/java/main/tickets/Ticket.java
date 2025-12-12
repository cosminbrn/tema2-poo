package main.tickets;

import lombok.Getter;
import lombok.Setter;
import main.database.Database;
import main.globals.ticketenums.TicketType;
import main.milestones.Milestone;
import main.tickets.actions.Action;
import main.tickets.actions.AssignAction;
import main.tickets.actions.DeassignAction;
import main.tickets.actions.AddToMilestoneAction;
import main.tickets.actions.RemoveFromDevAction;
import main.tickets.actions.StatusChangeAction;
import main.globals.ticketenums.ActionType;
import main.globals.ticketenums.BusinessPriority;
import main.globals.userenums.ExpertiseArea;
import main.globals.ticketenums.Status;
import main.users.Developer;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
    private BusinessPriority previousBusinessPriority;
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

    protected Ticket(final Builder<?> builder) {
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
        private static final int ARG_BY = 0;
        private static final int ARG_TIMESTAMP = 1;
        private static final int ARG_FROM = 2;
        private static final int ARG_TO = 3;
        private static final int ARG_MILESTONE = 4;

        /**
         * Static Factory Method to create Actions based on their type.
         * @param type the type of action to create
         * @param args the String args array
         * @return the created Action object
         */
        public static Action createAction(final ActionType type, final String... args) {
            return switch (type) {
                case ASSIGNED -> new AssignAction(args[ARG_BY], args[ARG_TIMESTAMP]);
                case DE_ASSIGNED -> new DeassignAction(args[ARG_BY], args[ARG_TIMESTAMP]);
                case STATUS_CHANGED -> new StatusChangeAction(args[ARG_BY],
                            args[ARG_TIMESTAMP], Status.fromString(args[ARG_FROM]),
                        Status.fromString(args[ARG_TO]));
                case ADDED_TO_MILESTONE -> new AddToMilestoneAction(args[ARG_BY],
                        args[ARG_TIMESTAMP], args[ARG_MILESTONE]);
                case REMOVED_FROM_DEV -> new RemoveFromDevAction(args[ARG_BY],
                        args[ARG_TIMESTAMP], args[ARG_FROM]);
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
        private final List<Comment> comments = new ArrayList<>();
        private final List<Action> actions = new ArrayList<>();

        /**
         * Set the assignedAt timestamp.
         * @param assignedAtValue assigned at timestamp
         * @return builder instance
         */
        public T setAssignedAt(final String assignedAtValue) {
            this.assignedAt = assignedAtValue;
            return self();
        }

        /**
         * Set the solvedAt timestamp.
         * @param solvedAtValue solved at timestamp
         * @return builder instance
         */
        public T setSolvedAt(final String solvedAtValue) {
            this.solvedAt = solvedAtValue;
            return self();
        }

        /**
         * Set the ticket id.
         * @param idValue ticket id
         * @return builder instance
         */
        public T setId(final int idValue) {
            this.id = idValue;
            return self();
        }

        /**
         * Set the username of the assignee.
         * @param assignedToValue assignee username
         * @return builder instance
         */
        public T setAssignedTo(final String assignedToValue) {
            this.assignedTo = assignedToValue;
            return self();
        }

        /**
         * Set the ticket type.
         * @param typeValue ticket type
         * @return builder instance
         */
        public T setType(final TicketType typeValue) {
            this.type = typeValue;
            return self();
        }

        /**
         * Set the ticket title.
         * @param titleValue ticket title
         * @return builder instance
         */
        public T setTitle(final String titleValue) {
            this.title = titleValue;
            return self();
        }

        /**
         * Set the business priority.
         * @param businessPriorityValue business priority
         * @return builder instance
         */
        public T setBusinessPriority(final BusinessPriority businessPriorityValue) {
            this.businessPriority = businessPriorityValue;
            return self();
        }

        /**
         * Set the ticket status.
         * @param statusValue ticket status
         * @return builder instance
         */
        public T setStatus(final Status statusValue) {
            this.status = statusValue;
            return self();
        }

        /**
         * Set the expertise area required.
         * @param expertiseAreaValue expertise area
         * @return builder instance
         */
        public T setExpertiseArea(final ExpertiseArea expertiseAreaValue) {
            this.expertiseArea = expertiseAreaValue;
            return self();
        }

        /**
         * Set the reporter username.
         * @param reportedByValue reporter username
         * @return builder instance
         */
        public T setReportedBy(final String reportedByValue) {
            this.reportedBy = reportedByValue;
            return self();
        }

        /**
         * Set the description text.
         * @param descriptionValue ticket description
         * @return builder instance
         */
        public T setDescription(final String descriptionValue) {
            this.description = descriptionValue;
            return self();
        }

        /**
         * Set the creation timestamp.
         * @param createdAtValue creation timestamp
         * @return builder instance
         */
        public T setCreatedAt(final String createdAtValue) {
            this.createdAt = createdAtValue;
            return self();
        }

        protected abstract T self();

        /**
         * Build the Ticket object.
         * @return the constructed Ticket
         */
        public abstract Ticket build();
    }

    /**
     * Advance business priority one step up.
     */
    public void updatePriority() {
        if (this.businessPriority == BusinessPriority.LOW) {
            this.businessPriority = BusinessPriority.MEDIUM;
        } else if (this.businessPriority == BusinessPriority.MEDIUM) {
            this.businessPriority = BusinessPriority.HIGH;
        } else if (this.businessPriority == BusinessPriority.HIGH) {
            this.businessPriority = BusinessPriority.CRITICAL;
        }
    }

    /**
     * Add a comment to the ticket.
     * @param username author username
     * @param comment comment text
     * @param timestamp creation timestamp
     */
    public void addComment(final String username, final String comment, final String timestamp) {
        this.comments.add(new Comment(username, comment, timestamp));
    }

    /**
     * Get comments authored by the specified user.
     * @param username author username
     * @return list of comments by the user
     */
    public List<Comment> getCommentsByUser(final String username) {
        List<Comment> userComments = new ArrayList<>();
        for (Comment comment : this.comments) {
            if (comment.author().equalsIgnoreCase(username)) {
                userComments.add(comment);
            }
        }
        return userComments;
    }

    /**
     * Remove the last comment authored by the specified user.
     * @param username author username
     */
    public void removeLastCommentByUser(final String username) {
        for (int i = comments.size() - 1; i >= 0; i--) {
            if (comments.get(i).author().equalsIgnoreCase(username)) {
                comments.remove(i);
                break;
            }
        }
    }

    /**
     * Update ticket status following the workflow.
     * @param currentDay the current day timestamp
     * @return the new status
     */
    public Status updateStatus(final String currentDay) {
        previousBusinessPriority = businessPriority;
        if (this.status == Status.OPEN) {
            this.status = Status.IN_PROGRESS;
        } else if (this.status == Status.IN_PROGRESS) {
            this.status = Status.RESOLVED;
            this.solvedAt = currentDay;
        } else if (this.status == Status.RESOLVED) {
            Database db = Database.getInstance();
            this.status = Status.CLOSED;
            if (this.assignedTo.isEmpty()) {
                this.solvedAt = currentDay;
            }
            ((Developer) db.getUserByUsername(this.assignedTo)).addClosedTicket(this);
            Milestone ticketMilestone = db.getMilestoneByName(getAssignedMilestone());
            ticketMilestone.closeTicket(this);
            ticketMilestone.updateMilestone(LocalDate.parse(currentDay));
            ticketMilestone.checkForTicketPriorityUpdates(LocalDate.parse(currentDay));
        }
        return this.status;
    }

    /**
     * Undo the current ticket status following the workflow.
     * @return the new status
     */
    public Status undoStatus() {
        previousBusinessPriority = businessPriority;
        if (this.status == Status.CLOSED) {
            Database db = Database.getInstance();
            this.status = Status.RESOLVED;
            this.solvedAt = "";
            ((Developer) db.getUserByUsername(this.assignedTo)).removeClosedTicket(this);
        } else if (this.status == Status.RESOLVED) {
            this.status = Status.IN_PROGRESS;
            this.solvedAt = "";
        }
        return this.status;
    }

    private <T> void executeAddAction(final ActionType actionType, final String by,
                                      final String timestamp,
                              final T from, final Status to, final String milestone) {

        String fromStr = from != null ? from.toString() : "";
        String toStr = to != null ? to.toString() : "";

        Action action = ActionFactory.createAction(actionType, by, timestamp,
                fromStr, toStr, milestone);
        this.actions.add(action);
    }

    /**
     * Add a generic action without status/milestone details.
     * @param actionType action type
     * @param by actor username
     * @param timestamp action timestamp
     */
    public void addAction(final ActionType actionType, final String by,
                          final String timestamp) {
        executeAddAction(actionType, by, timestamp, null, null, "");
    }

    /**
     * Add a status change action.
     * @param actionType action type
     * @param by actor username
     * @param timestamp action timestamp
     * @param from previous status
     * @param to new status
     */
    public void addAction(final ActionType actionType, final String by,
                          final String timestamp, final Status from, final Status to) {
        executeAddAction(actionType, by, timestamp, from, to, "");
    }

    /**
     * Add an action that includes either removed-from-dev or added-to-milestone specifics.
     * @param actionType action type
     * @param by actor username
     * @param timestamp action timestamp
     * @param arg extra argument (dev or milestone)
     */
    public void addAction(final ActionType actionType, final String by,
                          final String timestamp, final String arg) {
        if (actionType == ActionType.REMOVED_FROM_DEV) {
            executeAddAction(actionType, by, timestamp, arg, null, "");
        } else if (actionType == ActionType.ADDED_TO_MILESTONE) {
            executeAddAction(actionType, by, timestamp, null, null, arg);
        } else {
            throw new IllegalArgumentException(actionType.toString());
        }
    }

    /**
     * Compute resolution time in days.
     * @return resolution time in days
     */
    public int getResolutionTime() {
        return 1 + (int) ChronoUnit.DAYS.between(LocalDate.parse(this.getAssignedAt()),
                LocalDate.parse(this.getSolvedAt()));
    }

    /**
     * Creates a deep copy of the ticket.
     * @return a new Ticket object that is a deep copy of the current ticket
     */
    public abstract Ticket deepCopy();
}
