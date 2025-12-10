package main.command.commands.generate.performance.performancestrategy;

import main.tickets.Ticket;
import main.users.Developer;

import java.util.List;

import static main.command.commands.generate.performance.performancestrategy.SeniorityBonus.MID;

/**
 * Strategy for calculating performance score for mid-level developers.
 */
public class MidPerformanceStrategy implements PerformanceScoreStrategy {

    /**
     * Calculate the performance score for a mid-level developer for the given day.
     * @param dev the developer to evaluate
     * @param currentDay the current day string used by the strategy
     * @return the performance score
     */
    @Override
    public double calculatePerformanceScore(final Developer dev, final String currentDay) {
        List<Ticket> closedTicketsLastMonth = dev.getClosedTicketsFromLastMonth(currentDay);
        double avg = PerformanceScoreStrategy.averageResolutionTime(closedTicketsLastMonth);
        double highPriorityTickets =
                PerformanceScoreStrategy.getHighPriorityTickets(closedTicketsLastMonth);
        int closedTicketsCount = closedTicketsLastMonth.size();

        return Math.round(PERCENT_MULTIPLIER * (MID.getBonusPoints() + Math.max(0,
                POINT_FIVE * closedTicketsCount + POINT_SEVEN
                        * highPriorityTickets - POINT_THREE * avg)))
                / PERCENT_MULTIPLIER;
    }
}
