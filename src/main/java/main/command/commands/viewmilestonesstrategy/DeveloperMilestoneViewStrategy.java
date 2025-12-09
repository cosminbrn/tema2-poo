package main.command.commands.viewmilestonesstrategy;

import main.database.Database;
import main.milestones.Milestone;
import main.users.User;

import java.util.ArrayList;
import java.util.List;

public class DeveloperMilestoneViewStrategy implements  MilestoneFilteringStrategy {
    Database db = Database.getInstance();
    @Override
    public List<Milestone> getMilestones(User user) {
        List<Milestone> result = new ArrayList<>();
        for (Milestone milestone : db.getMilestones()) {
            for (String assignedDev : milestone.getAssignedDevs()) {
                if (assignedDev.equals(user.getUsername())) {
                    result.add(milestone);
                    break;
                }
            }
        }
        return result;
    }
}
