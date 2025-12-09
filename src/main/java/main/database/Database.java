package main.database;

import lombok.Getter;
import main.fileio.UserInput;
import main.milestones.Milestone;
import main.tickets.Ticket;
import main.users.User;
import main.users.UserFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class representing the database.
 * TODO: javadoc la toate metodele
 */
public class Database {
    private static Database instance;

    private int nextTicketID = 0;

    private List<User> users;
    @Getter
    private List<Ticket> tickets;
    @Getter
    private List<Milestone> milestones;

    private Database() {

    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public static void init() {
        getInstance();
        instance.users = new ArrayList<>();
        instance.tickets = new ArrayList<>();
        instance.milestones = new ArrayList<>();
    }

    public void reset() {
        instance = null;
    }

    public void addUser(User user) {
        getInstance().users.add(user);
    }

    public void addTicket(Ticket ticket) {
        getInstance().tickets.add(ticket);
    }

    public void addMilestone(Milestone milestone) {
        getInstance().milestones.add(milestone);
    }

    public void loadUsers(ArrayList<UserInput> users) {
        for (UserInput userInput : users) {
            addUser(UserFactory.createUser(userInput));
        }
    }

    public int getNextTicketId() {
        return nextTicketID++;
    }

    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public Ticket getTicketById(int ticketID) {
        for (Ticket ticket : tickets) {
            if (ticket.getId() == ticketID) {
                return ticket;
            }
        }
        return null;
    }

    public List<Ticket> getTicketsByIds(int[] ticketIds) {
        List<Ticket> result = new ArrayList<>();
        for (int id : ticketIds) {
            Ticket ticket = getTicketById(id);
            if (ticket != null) {
                result.add(ticket);
            }
        }
        return result;
    }

    public Milestone getMilestoneByName(String milestoneName) {
        for (Milestone milestone : milestones) {
            if (milestone.getName().equals(milestoneName)) {
                return milestone;
            }
        }
        return null;
    }

    public void updateMilestones(LocalDate currentDay) {
        if (milestones == null) {
            return;
        }
        for (Milestone milestone : milestones) {
            milestone.updateMilestone(currentDay);
        }
    }

    public void updateDatabase(LocalDate currentDay) {
        updateMilestones(currentDay);
    }

    public List<Milestone> getMilestonesByDeveloper(String developerUsername) {
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
