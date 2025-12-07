package main.database;

import lombok.Getter;
import main.fileio.CommandInput;
import main.fileio.UserInput;
import main.tickets.Ticket;
import main.users.User;
import main.users.UserFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class representing the database.
 */
public class Database {

    private static Database instance;

    private int nextTicketID = 0;

    private List<User> users;
    @Getter
    private List<Ticket> tickets;
    //private List<Milestone> milestones;

    private Database() {

    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
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

//    public void addMilestone(Milestone milestone) {
//        getInstance().milestones.add(milestone);
//    }

    public void loadUsers(ArrayList<UserInput> users) {
        for (UserInput userInput : users) {
            addUser(UserFactory.createUser(userInput));
        }
    }

    public int getNextTicketID() {
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
}
