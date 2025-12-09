package main.milestones;

import lombok.Getter;
import main.database.Database;
import main.milestones.enums.MilestoneState;
import main.tickets.Ticket;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static main.milestones.enums.MilestoneState.ACTIVE;
import static main.milestones.enums.MilestoneState.COMPLETED;
import static main.tickets.enums.BusinessPriority.CRITICAL;

@Getter
public class Milestone {
    private final String name;
    private final String[] blockingFor;
    private final LocalDate dueDate;
    private final LocalDate createdAt;
    private final List<Integer> tickets;
    private final String[] assignedDevs;
    private final String createdBy;

    private MilestoneState status = ACTIVE;
    private boolean isBlocked;
    @Getter
    private List<Integer> openTickets;
    @Getter
    private List<Integer> closedTickets;
    private double completionPercentage;
    private Map<String, List<Integer>> repartition;

    private Milestone(Builder builder) {
        this.name = builder.name;
        this.blockingFor = builder.blockingFor;
        this.dueDate = builder.dueDate;
        this.createdAt = builder.createdAt;
        this.tickets = builder.tickets;
        this.assignedDevs = builder.assignedDevs;
        this.createdBy = builder.createdBy;
        this.isBlocked = false;
        this.openTickets = new ArrayList<>(builder.tickets);
        this.closedTickets = new ArrayList<>();
        this.completionPercentage = 0.0;
        this.repartition = new LinkedHashMap<>();
    }

    public static class Builder {
        private String name;
        private String[] blockingFor;
        private LocalDate dueDate;
        private LocalDate createdAt;
        private List<Integer> tickets = new ArrayList<>();
        private String[] assignedDevs;
        private String createdBy;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setBlockingFor(String[] blockingFor) {
            this.blockingFor = blockingFor;
            return this;
        }

        public Builder setDueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder setCreatedAt(LocalDate createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder setTickets(int[] tickets) {
            List<Integer> ticketList = new ArrayList<>();
            for (int ticket : tickets) {
                ticketList.add(ticket);
            }
            this.tickets = ticketList;
            return this;
        }

        public Builder setAssignedDevs(String[] assignedDevs) {
            this.assignedDevs = assignedDevs;
            return this;
        }

        public Builder setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Milestone build() {
            return new Milestone(this);
        }
    }

    public void updateMilestone(LocalDate currentDate) {
        if (isBlocked) {
            return;
        }

        if (calculateCompletionPercentage() == 1.0) {
            this.status = COMPLETED;
        }

        if (currentDate.isAfter(this.dueDate)) {
            setTicketPrioritiesToCritical();
            // TODO: send notification to assigned devs
        } else if (((int) ChronoUnit.DAYS.between(currentDate, dueDate) + 1) <= 2) {
            setTicketPrioritiesToCritical();
            // TODO: send special notification to assigned devs

        } else if (((int) ChronoUnit.DAYS.between(currentDate, dueDate) + 1) % 3 == 0) {
            updateTicketPriorities();
        }
    }

    public void updateTicketPriorities() {
        Database db = Database.getInstance();
        for (int ticket : tickets) {
            db.getTicketById(ticket).updatePriority();
        }
    }

    public void setTicketPrioritiesToCritical() {
        Database db = Database.getInstance();
        for (int ticket : tickets) {
            db.getTicketById(ticket).setBusinessPriority(CRITICAL);
        }
    }

    public void closeMilestone() {
        this.status = MilestoneState.CLOSED;
    }

    public void activateMilestone() {
        this.status = MilestoneState.ACTIVE;
    }

    public void blockMilestone() {
        this.isBlocked = true;
    }

    public void unblockMilestone() {
        this.isBlocked = false;
    }

    public int calculateDaysUntilDue(LocalDate currentDate) {
        return Math.max(0, (int) ChronoUnit.DAYS.between(currentDate, this.dueDate) + 1);
    }

    public int calculateOverdueBy(LocalDate currentDate) {
        return Math.max(0, 1 + (int) ChronoUnit.DAYS.between(this.dueDate, currentDate));
    }

    public double calculateCompletionPercentage() {
        if (closedTickets.isEmpty()) {
            this.completionPercentage = 0.0;
            return completionPercentage;
        }

        double n = (double) closedTickets.size() / (double) tickets.size();
        this.completionPercentage = (double) Math.round(n * 100.0) / 100;
        return this.completionPercentage;
    }

    private void removeTicketFromOpenTickets(int ticketId) {
        openTickets.removeIf(id -> id == ticketId);
    }

    private void addTicketToClosedTickets(int ticketId) {
        closedTickets.add(ticketId);
    }

    public void closeTicket(Ticket ticket) {
        int ticketId = ticket.getId();
        removeTicketFromOpenTickets(ticketId);
        addTicketToClosedTickets(ticketId);
        calculateCompletionPercentage();
    }
}
