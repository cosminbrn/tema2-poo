package main.tickets.actions;

import lombok.Getter;

import static main.globals.ticketenums.ActionType.ADDED_TO_MILESTONE;

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
    public AddToMilestoneAction(final String by,
                                final String timestamp,
                                final String milestoneName) {
        super(ADDED_TO_MILESTONE, by, timestamp);
        this.milestone = milestoneName;
    }

    protected AddToMilestoneAction(final AddToMilestoneAction other) {
        super(other);
        this.milestone = other.milestone;
    }

    /**
     * Create a deep copy of this add-to-milestone action.
     * @return a new {@code AddToMilestoneAction} instance with the same data
     */
    @Override
    public Action deepCopy() {
        return new AddToMilestoneAction(this);
    }
}
