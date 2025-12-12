package main.database;

import lombok.Getter;
import main.fileio.UserInput;
import main.milestones.Milestone;
import main.tickets.Ticket;
import main.globals.ticketenums.BusinessPriority;
import main.users.Developer;
import main.users.User;
import main.users.UserFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static main.globals.ticketenums.ActionType.REMOVED_FROM_DEV;
import static main.globals.ticketenums.Status.CLOSED;
import static main.globals.ticketenums.Status.IN_PROGRESS;
import static main.globals.ticketenums.Status.OPEN;
import static main.globals.ticketenums.Status.RESOLVED;

/**
 * Singleton class representing the database.
 */
public final class Database {

    private static Database instance;
    private int nextTicketID = 0;
    private List<User> users;
    @Getter
    private List<Ticket> tickets;
    @Getter
    private List<Milestone> milestones;

    /**
     * Singleton constructor.
     */
    private Database() {

    }

    /**
     * Gets the singleton instance of the database.
     * @return the database instance
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Initializes the database.
     */
    public static void init() {
        getInstance();
        instance.users = new ArrayList<>();
        instance.tickets = new ArrayList<>();
        instance.milestones = new ArrayList<>();
    }

    /**
     * Resets the database instance (for testing purposes).
     */
    public void reset() {
        instance = null;
    }

    /**
     * Adds a user to the database.
     * @param user the user to add
     */
    public void addUser(final User user) {
        getInstance().users.add(user);
    }

    /**
     * Adds a ticket to the database.
     * @param ticket the ticket to add
     */
    public void addTicket(final Ticket ticket) {
        getInstance().tickets.add(ticket);
    }

    /**
     * Adds a milestone to the database.
     * @param milestone the milestone to add
     */
    public void addMilestone(final Milestone milestone) {
        getInstance().milestones.add(milestone);
    }

    /**
     * Gets the next ticket ID and increments the counter.
     * @return the next ticket ID
     */
    public int getNextTicketId() {
        return nextTicketID++;
    }

    /**
     * Loads users into the database.
     * @param userInputs list of user inputs to add to the database
     */
    public void loadUsers(final ArrayList<UserInput> userInputs) {
        for (UserInput userInput : userInputs) {
            addUser(UserFactory.createUser(userInput));
        }
    }

    /**
     * Gets a user by username.
     * @param username the username
     * @return the user or null if not found
     */
    public User getUserByUsername(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Gets a ticket by its ID.
     * @param ticketID the ticket ID
     * @return the ticket or null if not found
     */
    public Ticket getTicketById(final int ticketID) {
        for (Ticket ticket : tickets) {
            if (ticket.getId() == ticketID) {
                return ticket;
            }
        }
        return null;
    }

    /**
     * Gets tickets by their IDs.
     * @param ticketIds array of ticket IDs
     * @return list of tickets
     */
    public List<Ticket> getTicketsByIds(final int[] ticketIds) {
        List<Ticket> result = new ArrayList<>();
        for (int ticketId : ticketIds) {
            Ticket ticket = getTicketById(ticketId);
            if (ticket != null) {
                result.add(ticket);
            }
        }
        return result;
    }

    /**
     * Gets tickets by their IDs.
     * @param ticketIds list of ticket IDs
     * @return list of tickets
     */
    public List<Ticket> getTicketsByIds(final List<Integer> ticketIds) {
        List<Ticket> result = new ArrayList<>();
        for (Integer ticketId : ticketIds) {
            Ticket ticket = getTicketById(ticketId);
            if (ticket != null) {
                result.add(ticket);
            }
        }
        return result;
    }

    /**
     * Gets a milestone by its name.
     * @param milestoneName the milestone name
     * @return the milestone or null if not found
     */
    public Milestone getMilestoneByName(final String milestoneName) {
        for (Milestone milestone : milestones) {
            if (milestone.getName().equals(milestoneName)) {
                return milestone;
            }
        }
        return null;
    }

    /**
     * Updates all milestones based on the current day.
     * @param currentDay the current day
     */
    public void updateMilestones(final LocalDate currentDay) {
        if (milestones == null) {
            return;
        }
        for (Milestone milestone : milestones) {
            milestone.updateMilestone(currentDay);
            milestone.checkForTicketPriorityUpdates(currentDay);
        }
    }

    /**
     * Checks ticket priorities and updates their status if necessary.
     * @param currentDay the current day
     */
    public void checkTicketPriorities(final LocalDate currentDay) {
        for (Ticket ticket : tickets) {
            BusinessPriority businessPriority = ticket.getBusinessPriority();
            Developer developer = (Developer) getUserByUsername(ticket.getAssignedTo());
            if (developer != null && !developer.hasAccess(businessPriority)) {
                developer.removeTicketFromAssigned(ticket);
                ticket.setStatus(OPEN);
                ticket.addAction(REMOVED_FROM_DEV, "system", currentDay.toString(),
                        developer.getUsername());
                ticket.setAssignedTo("");

            }
        }
    }

    /**
     * Gets all tickets that are either CLOSED or RESOLVED.
     * @return list of closed or resolved tickets
     */
    public List<Ticket> getClosedResolvedTickets() {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == RESOLVED || ticket.getStatus() == CLOSED) {
                result.add(ticket);
            }
        }
        return result;
    }

    /**
     * Gets all tickets that are either OPEN or IN_PROGRESS.
     * @return list of open or in-progress tickets
     */
    public List<Ticket> getOpenInProgressTickets() {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getStatus() == OPEN || ticket.getStatus() == IN_PROGRESS) {
                result.add(ticket);
            }
        }
        return result;
    }

    /**
     * Updates the database by updating milestones and checking ticket priorities.
     * @param currentDay the current day
     */
    public void updateDatabase(final LocalDate currentDay) {
        updateMilestones(currentDay);
        checkTicketPriorities(currentDay);
    }

    /**
     * Gets milestones assigned to a specific developer.
     * @param developerUsername the developer's username
     * @return list of milestones assigned to the developer
     */
    public List<Milestone> getMilestonesByDeveloper(final String developerUsername) {
        List<Milestone> result = new ArrayList<>();
        for (Milestone milestone : milestones) {
            for (String devUsername : milestone.getAssignedDevs()) {
                if (devUsername.equals(developerUsername)) {
                    result.add(milestone);
                    break;
                }
            }
        }
        return result;
    }
}
