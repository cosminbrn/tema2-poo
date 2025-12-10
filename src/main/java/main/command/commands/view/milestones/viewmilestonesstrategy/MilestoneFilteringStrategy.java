package main.command.commands.view.milestones.viewmilestonesstrategy;

import main.milestones.Milestone;
import main.users.User;

import java.util.List;

/**
 * Strategy interface for filtering milestones based on user type.
 */
public interface MilestoneFilteringStrategy {
    /**
     * Filters milestones for the given user.
     *
     * @param user the user for whom to filter milestones
     * @return a list of milestones relevant to the user
     */
    List<Milestone> getMilestones(User user);
}
