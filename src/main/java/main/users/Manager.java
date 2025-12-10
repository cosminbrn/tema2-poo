package main.users;

import lombok.Getter;
import main.database.Database;
import main.milestones.Milestone;

import java.util.ArrayList;
import java.util.List;

import static main.users.enums.Role.MANAGER;

/**
 * Class representing a manager user.
 */
@Getter
public final class Manager extends User {
    private final String hireDate;
    private final String[] subordinates;
    private final List<Milestone> createdMilestones = new ArrayList<>();

    public Manager(final String name, final String email,
                   final String hireDate, final String[] subordinates) {
        super(name, email, MANAGER);
        this.hireDate = hireDate;
        this.subordinates = subordinates;
    }

    public void addCreatedMilestone(final Milestone milestone) {
        this.createdMilestones.add(milestone);
    }

    public List<Developer> getSubordinateDevelopers() {
        Database db = Database.getInstance();
        List<Developer> subordinateUsers = new ArrayList<>();
        for (String username : subordinates) {
            Developer user = (Developer) db.getUserByUsername(username);
            if (user != null) {
                subordinateUsers.add(user);
            }
        }
        return subordinateUsers;
    }
}
