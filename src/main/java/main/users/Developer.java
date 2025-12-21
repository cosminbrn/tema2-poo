package main.users;

import lombok.Getter;
import lombok.Setter;
import main.database.Database;
import main.globals.ticketenums.Status;
import main.globals.userenums.ExpertiseArea;
import main.globals.Observer;
import main.milestones.Milestone;
import main.tickets.Ticket;
import main.globals.ticketenums.BusinessPriority;
import main.globals.userenums.Seniority;

import java.util.ArrayList;
import java.util.List;

import static main.globals.userenums.Role.DEVELOPER;

/**
 * Class representing a developer user.
 */
@Getter
public final class Developer extends User implements Observer {
    private final String hireDate;
    private final ExpertiseArea expertiseArea;
    private final Seniority seniority;

    private final List<Ticket> assignedTickets = new ArrayList<>();
    private final List<Ticket> previouslyAssignedTickets = new ArrayList<>();
    private final List<Ticket> closedTickets = new ArrayList<>();
    private final List<String> notifications = new ArrayList<>();

    @Getter @Setter
    private double performanceScore;

    public Developer(final String name, final String email, final String hireDate,
                         final ExpertiseArea expertiseArea, final Seniority seniority) {
        super(name, email, DEVELOPER);
        this.hireDate = hireDate;
        this.expertiseArea = expertiseArea;
        this.seniority = seniority;
    }

    /**
     * Checks if the developer has expertise in the given area.
     * @param ticketExpertiseArea the expertise area to check
     * @return true if the developer has expertise in the area, false otherwise
     */
    public boolean hasExpertise(final ExpertiseArea ticketExpertiseArea) {
        if (this.expertiseArea == ExpertiseArea.FRONTEND) {
            return ticketExpertiseArea == ExpertiseArea.FRONTEND || ticketExpertiseArea == ExpertiseArea.DESIGN;
        } else if (this.expertiseArea == ExpertiseArea.DESIGN) {
            return ticketExpertiseArea == ExpertiseArea.DESIGN || ticketExpertiseArea == ExpertiseArea.FRONTEND;
        } else if (this.expertiseArea == ExpertiseArea.BACKEND) {
            return ticketExpertiseArea == ExpertiseArea.BACKEND || ticketExpertiseArea == ExpertiseArea.DB;
        } else if (this.expertiseArea == ExpertiseArea.DB) {
            return ticketExpertiseArea == ExpertiseArea.DB;
        } else if (this.expertiseArea == ExpertiseArea.DEVOPS) {
            return ticketExpertiseArea == ExpertiseArea.DEVOPS;
        } else return this.expertiseArea == ExpertiseArea.FULLSTACK;
    }

    /**
     * Checks if the developer has access to handle tickets of the given business priority.
     * @param ticketBusinessPriority the business priority to check
     * @return true if the developer has access, false otherwise
     */
    public boolean hasAccess(final BusinessPriority ticketBusinessPriority) {
        if (this.seniority == Seniority.JUNIOR) {
            return ticketBusinessPriority == BusinessPriority.LOW
                    || ticketBusinessPriority == BusinessPriority.MEDIUM;
        } else if (this.seniority == Seniority.MID) {
            return ticketBusinessPriority != BusinessPriority.CRITICAL;
        } else {
            return this.seniority == Seniority.SENIOR;
        }
    }

    /**
     * Checks if the developer is assigned to the given milestone.
     * @param milestoneName the name of the milestone
     * @return true if the developer is assigned, false otherwise
     */
    public boolean isAssignedToMilestone(final String milestoneName) {
        Database db = Database.getInstance();
        Milestone milestone =  db.getMilestoneByName(milestoneName);
        for (String developerUsername : milestone.getAssignedDevs()) {
            if (developerUsername.equalsIgnoreCase(this.getUsername())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a ticket to the list of assigned tickets.
     * @param ticket the ticket to add
     */
    public void addAssignedTicket(final Ticket ticket) {
        this.assignedTickets.add(ticket);
    }

    /**
     * Returns the ticket with the given ID if it is assigned to the developer.
     * @param ticketId the ticket ID
     * @return the ticket with the given ID if it is assigned to the developer, null otherwise
     */
    public Ticket getAssignedTicketById(final int ticketId) {
        for (Ticket ticket : this.assignedTickets) {
            if (ticket.getId() == ticketId) {
                return ticket;
            }
        }
        return null;
    }

    /**
     * Adds a ticket to the list of closed tickets.
     * @param ticket the ticket to add
     */
    public void addClosedTicket(final Ticket ticket) {
        this.closedTickets.add(ticket);
    }

    /**
     * Removes a ticket from the list of closed tickets.
     * @param ticket the ticket to remove
     */
    public void removeClosedTicket(final Ticket ticket) {
        this.closedTickets.remove(ticket);
    }

    /**
     * Removes a ticket from the list of assigned tickets.
     * @param ticket the ticket to remove
     */
    public void removeTicketFromAssigned(final Ticket ticket) {
        this.assignedTickets.remove(ticket);
    }

    /**
     * Adds a ticket to the list of previously assigned tickets.
     * @param ticket the ticket to add
     */
    public void addPreviouslyAssignedTicket(final Ticket ticket) {
        this.previouslyAssignedTickets.add(ticket);
    }

    /**
     * Returns tickets closed in the last month.
     * @param currentDay current day timestamp
     * @return list of tickets closed in the last month
     */
    public List<Ticket> getClosedTicketsFromLastMonth(final String currentDay) {
        List<Ticket> result = new ArrayList<>();
        int currentMonth = Integer.parseInt(currentDay.split("-")[1]);
        for (Ticket ticket : this.closedTickets) {
            int ticketMonth = Integer.parseInt(ticket.getSolvedAt().split("-")[1]);
            if (ticketMonth == currentMonth - 1) {
                result.add(ticket);
            }
        }
        return result;
    }


    /**
     *  Updates the developer's notifications.
     * @param notification the notification message
     */
    @Override
    public void update(final String notification) {
        this.notifications.add(notification);
    }

    /**
     * Clears the developer's notifications.
     */
    public void clearNotifications() {
        this.notifications.clear();
    }

    /**
     * Gets the milestones assigned to the developer.
     * @return list of milestones assigned to the developer
     */
    public List<Milestone> getAssignedMilestones() {
        return Database.getInstance().getMilestonesByDeveloper(this.getUsername());
    }

    /**
     * Returns all OPEN tickets from milestones assigned to this developer.
     * @return list of open tickets from the developer's milestones
     */
    public List<Ticket> getOpenTicketsFromAssignedMilestones() {
        Database db = Database.getInstance();
        List<Milestone> milestones = getAssignedMilestones();
        List<Ticket> result = new ArrayList<>();
        for (Milestone milestone : milestones) {
            List<Ticket> seeminglyOpenTickets = db.getTicketsByIds(milestone.getTickets());
            for (Ticket ticket : seeminglyOpenTickets) {
                if (ticket.getStatus() == Status.OPEN) {
                    result.add(ticket);
                }
            }
        }
        return result;
    }
}
