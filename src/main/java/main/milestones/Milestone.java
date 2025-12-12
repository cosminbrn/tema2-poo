package main.milestones;

import lombok.Getter;
import main.database.Database;
import main.globals.Observable;
import main.globals.Observer;
import main.globals.milestoneenums.MilestoneState;
import main.tickets.Ticket;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static main.globals.milestoneenums.MilestoneMessage.MILESTONE_ALMOST_DUE;
import static main.globals.milestoneenums.MilestoneMessage.MILESTONE_OPENED;
import static main.globals.milestoneenums.MilestoneMessage.MILESTONE_UNLOCKED_OVERDUE;
import static main.globals.milestoneenums.MilestoneState.ACTIVE;
import static main.globals.milestoneenums.MilestoneState.COMPLETED;
import static main.globals.ticketenums.BusinessPriority.CRITICAL;

/**
 * Big class representing a Milestone in the project management system.
 */
@Getter
public final class Milestone implements Observable {
    private final String name;
    private final String[] blockingFor;
    private final LocalDate dueDate;
    private final LocalDate createdAt;
    private final List<Integer> tickets;
    private final String[] assignedDevs;
    private final String createdBy;
    private LocalDate completedAt;

    private MilestoneState status = ACTIVE;
    private boolean isBlocked;
    @Getter
    private final List<Integer> openTickets;
    private final LinkedList<Integer> closedTickets;
    private double completionPercentage;
    private final Map<String, List<Integer>> repartition;

    private final List<Observer> observers = new ArrayList<>();

    private static final int ALMOST_DUE_DAYS = 2;
    private static final int DAYS_MODULO = 3;
    private static final double PERCENT_FACTOR = 100.0;

    private Milestone(final Builder builder) {
        this.name = builder.name;
        this.blockingFor = builder.blockingFor;
        this.dueDate = builder.dueDate;
        this.createdAt = builder.createdAt;
        this.tickets = builder.tickets;
        this.assignedDevs = builder.assignedDevs;
        this.createdBy = builder.createdBy;
        this.isBlocked = false;
        this.openTickets = new ArrayList<>(builder.tickets);
        this.closedTickets = new LinkedList<>();
        this.completionPercentage = 0.0;
        this.repartition = new LinkedHashMap<>();
    }

    // Observer logic
    @Override
    public void addObserver(final Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(final Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(final String notification) {
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }

    /**
     * Builder class for constructing Milestone instances.
     */
    public static class Builder {
        private String name;
        private String[] blockingFor;
        private LocalDate dueDate;
        private LocalDate createdAt;
        private List<Integer> tickets = new ArrayList<>();
        private String[] assignedDevs;
        private String createdBy;

        /**
         * Set milestone name.
         * @param name milestone name
         * @return builder instance
         */
        public Builder setName(final String name) {
            this.name = name;
            return this;
        }

        /**
         * Set blocking milestones.
         * @param blockingFor names of milestones this one blocks
         * @return builder instance
         */
        public Builder setBlockingFor(final String[] blockingFor) {
            this.blockingFor = blockingFor;
            return this;
        }

        /**
         * Set due date for the milestone.
         * @param dueDate due date
         * @return builder instance
         */
        public Builder setDueDate(final LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        /**
         * Set creation date for the milestone.
         * @param createdAt creation date
         * @return builder instance
         */
        public Builder setCreatedAt(final LocalDate createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        /**
         * Set tickets assigned to this milestone.
         * @param tickets array of ticket ids
         * @return builder instance
         */
        public Builder setTickets(final int[] tickets) {
            List<Integer> ticketList = new ArrayList<>();
            for (int ticket : tickets) {
                ticketList.add(ticket);
            }
            this.tickets = ticketList;
            return this;
        }

        /**
         * Set assigned developers for the milestone.
         * @param assignedDevs array of developer usernames
         * @return builder instance
         */
        public Builder setAssignedDevs(final String[] assignedDevs) {
            this.assignedDevs = assignedDevs;
            return this;
        }

        /**
         * Set creator username for the milestone.
         * @param createdBy creator username
         * @return builder instance
         */
        public Builder setCreatedBy(final String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        /**
         * Build the milestone instance.
         * @return constructed Milestone
         */
        public Milestone build() {
            return new Milestone(this);
        }
    }

    /**
     * Update milestone state and notify observers when relevant.
     * @param currentDate the current date
     */
    public void updateMilestone(final LocalDate currentDate) {
        if (isBlocked) {
            return;
        }

        if (calculateCompletionPercentage() == 1.0) {
            Database db = Database.getInstance();
            completeMilestone(currentDate);
            for (String milestoneToUnlock : blockingFor) {
                Milestone blockedMilestone = db.getMilestoneByName(milestoneToUnlock);
                if (blockedMilestone != null && blockedMilestone.isBlocked()) {
                    blockedMilestone.unblockMilestone(currentDate, db.getTicketById(this.closedTickets.getLast()));
                }
            }
        }

        int daysUntil = (int) ChronoUnit.DAYS.between(currentDate, dueDate) + 1;
        if (daysUntil == ALMOST_DUE_DAYS) {
            setTicketPrioritiesToCritical();
            notifyObservers(String.format(MILESTONE_ALMOST_DUE.getMessage(), this.name));
        } else if (daysUntil % DAYS_MODULO == 0) {
            updateTicketPriorities();
        }
    }

    /**
     * Update priorities for all tickets in the milestone.
     */
    public void updateTicketPriorities() {
        Database db = Database.getInstance();
        for (int ticket : tickets) {
            db.getTicketById(ticket).updatePriority();
        }
    }

    /**
     * Set all ticket priorities in the milestone to CRITICAL.
     */
    public void setTicketPrioritiesToCritical() {
        Database db = Database.getInstance();
        for (int ticket : tickets) {
            db.getTicketById(ticket).setBusinessPriority(CRITICAL);
        }
    }

    /**
     * Close this milestone.
     */
    public void completeMilestone(LocalDate currentDate) {
        this.status = COMPLETED;
        if (this.completedAt == null) {
            this.completedAt = currentDate;
        }
    }

    /**
     * Activate this milestone.
     */
    public void activateMilestone() {
        this.status = MilestoneState.ACTIVE;
    }

    /**
     * Block this milestone.
     */
    public void blockMilestone() {
        this.isBlocked = true;
    }

    /**
     * Unblock this milestone and notify observers.
     * @param currentDate current date used to determine overdue stat
     * @param lastClosedTicket last closed ticket in the deblocking milestone
     */
    public void unblockMilestone(final LocalDate currentDate, Ticket lastClosedTicket) {
        if (currentDate.isAfter(this.dueDate)) {
            setTicketPrioritiesToCritical();
            notifyObservers(String.format(MILESTONE_UNLOCKED_OVERDUE.getMessage(), this.name));
        } else {
            notifyObservers(String.format(MILESTONE_OPENED.getMessage(),
                    this.name, lastClosedTicket.getId()));
        }
        this.isBlocked = false;
    }

    /**
     * Calculate the number of days until due (min 0).
     * @param currentDate reference date
     * @return days until due
     */
    public int calculateDaysUntilDue(final LocalDate currentDate) {
        return Math.max(0, (int) ChronoUnit.DAYS.between(currentDate, this.dueDate) + 1);
    }

    /**
     * Calculate how many days overdue the milestone is (min 0).
     * @param currentDate reference date
     * @return overdue days
     */
    public int calculateOverdueBy(final LocalDate currentDate) {
        if (completedAt == null) {
            return Math.max(0, (int) ChronoUnit.DAYS.between(this.dueDate, currentDate) + 1);
        }
        return Math.max(0, 1 + (int) ChronoUnit.DAYS.between(this.dueDate, this.completedAt));
    }

    /**
     * Calculate completion percentage as closed/tickets, rounded to 2 decimals.
     * @return completion percentage between 0 and 1
     */
    public double calculateCompletionPercentage() {
        if (closedTickets.isEmpty()) {
            this.completionPercentage = 0.0;
            return completionPercentage;
        }

        double n = (double) closedTickets.size() / (double) tickets.size();
        this.completionPercentage = (double) Math.round(n * PERCENT_FACTOR) / PERCENT_FACTOR;
        return this.completionPercentage;
    }

    private void removeTicketFromOpenTickets(final int ticketId) {
        openTickets.removeIf(id -> id == ticketId);
    }

    private void addTicketToClosedTickets(final int ticketId) {
        closedTickets.add(ticketId);
    }

    /**
     * Close a ticket inside this milestone and update completion percentage.
     * @param ticket the ticket to close
     */
    public void closeTicket(final Ticket ticket) {
        int ticketId = ticket.getId();
        removeTicketFromOpenTickets(ticketId);
        addTicketToClosedTickets(ticketId);
        calculateCompletionPercentage();
    }
    public List<Integer> getSortedClosedTickets() {
        List<Integer> result = new ArrayList<>(this.closedTickets);
        result.sort(Comparator.naturalOrder());
        return result;
    }
}
