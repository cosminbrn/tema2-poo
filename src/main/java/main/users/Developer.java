package main.users;

import lombok.Getter;
import main.database.Database;
import main.globals.ExpertiseArea;
import main.milestones.Milestone;
import main.tickets.Ticket;
import main.tickets.enums.BusinessPriority;
import main.users.enums.Seniority;

import java.util.ArrayList;
import java.util.List;

import static main.globals.ExpertiseArea.*;
import static main.users.enums.Role.DEVELOPER;

/**
 * Class representing a developer user.
 */
@Getter
public final class Developer extends User {
    private final String hireDate;
    private final ExpertiseArea expertiseArea;
    private final Seniority seniority;

    private final List<Ticket> assignedTickets = new ArrayList<>();

    public Developer(final String name, final String email, final String hireDate,
                         final ExpertiseArea expertiseArea, final Seniority seniority) {
        super(name, email, DEVELOPER);
        this.hireDate = hireDate;
        this.expertiseArea = expertiseArea;
        this.seniority = seniority;
    }

    public boolean hasExpertise(final ExpertiseArea ticketExpertiseArea) {
        if (this.expertiseArea == FRONTEND) {
            return ticketExpertiseArea == FRONTEND || ticketExpertiseArea == DESIGN;
        } else if (this.expertiseArea == DESIGN) {
            return ticketExpertiseArea == DESIGN || ticketExpertiseArea == FRONTEND;
        } else if (this.expertiseArea == BACKEND) {
            return ticketExpertiseArea == BACKEND || ticketExpertiseArea == DB;
        } else if (this.expertiseArea == DB) {
            return ticketExpertiseArea == DB;
        } else if (this.expertiseArea == DEVOPS) {
            return ticketExpertiseArea == DEVOPS;
        }
        return true;
    }

    public boolean hasAccess(final BusinessPriority ticketBusinessPriority) {
        if (this.seniority == Seniority.JUNIOR) {
            return ticketBusinessPriority == BusinessPriority.LOW ||
                   ticketBusinessPriority == BusinessPriority.MEDIUM;
        } else if (this.seniority == Seniority.MID) {
            return ticketBusinessPriority != BusinessPriority.CRITICAL;
        } else return this.seniority == Seniority.SENIOR;
    }

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

    public void addAssignedTicket(final Ticket ticket) {
        this.assignedTickets.add(ticket);
    }

    public Ticket getLastAssignedTicket() {
        return this.assignedTickets.getLast();
    }

    public Ticket getAssignedTicketById(final int ticketId) {
        for (Ticket ticket : this.assignedTickets) {
            if (ticket.getId() == ticketId) {
                return ticket;
            }
        }
        return null;
    }

    public void removeTicketFromAssigned(final Ticket ticket) {
        this.assignedTickets.remove(ticket);
    }
}
