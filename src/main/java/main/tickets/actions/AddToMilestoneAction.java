package main.tickets.actions;

import lombok.Getter;

import static main.tickets.enums.ActionType.ADDED_TO_MILESTONE;

/**
 * Class representing an action of adding a ticket to a milestone.
 */
@Getter
public class AddToMilestoneAction extends Action  {
    private final String milestone;

    /**
     * Constructor for AddToMilestoneAction.
     * @param by User who performed the action
     * @param timestamp Time when the action was performed
     * @param milestoneName Name of the milestone the ticket was added to
     */
    public AddToMilestoneAction(String by, String timestamp, String milestoneName) {
        super(ADDED_TO_MILESTONE, by, timestamp);
        this.milestone = milestoneName;
    }
}
