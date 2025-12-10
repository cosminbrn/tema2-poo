package main.command.commands.view.milestones.viewmilestonesstrategy;

import main.database.Database;
import main.milestones.Milestone;
import main.users.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Milestone view strategy for developers: shows milestones assigned to the developer.
 */
public class ManagerMilestoneViewStrategy implements MilestoneFilteringStrategy {
    Database db = Database.getInstance();


    @Override
    public List<Milestone> getMilestones(final User user) {
        List<Milestone> result = new ArrayList<>();
        for (Milestone milestone : db.getMilestones()) {
            if (milestone.getCreatedBy().equals(user.getUsername())) {
                result.add(milestone);
            }
        }
        return result;
    }
}
