package main.command.commands.viewmilestoneshelpers;

import main.database.Database;
import main.milestones.Milestone;
import main.users.User;

import java.util.ArrayList;
import java.util.List;

public class ManagerMilestoneViewStrategy implements MilestoneFilteringStrategy {
    Database db = Database.getInstance();


    @Override
    public List<Milestone> getMilestones(User user) {
        List<Milestone> result = new ArrayList<>();
        for (Milestone milestone : db.getMilestones()) {
            if (milestone.getCreatedBy().equals(user.getUsername())) {
                result.add(milestone);
            }
        }
        return result;
    }
}
