package main.command.commands.generate.performance.performancestrategy;

import main.tickets.Ticket;
import main.users.Developer;

import java.util.List;

import static main.command.commands.generate.performance.performancestrategy.SeniorityBonus.SENIOR;

/**
 * Strategy for calculating performance score for senior developers.
 */
public class SeniorPerformanceStrategy implements PerformanceScoreStrategy {

    /**
     * Calculate the performance score for a senior developer for the given day.
     * @param dev the developer to evaluate
     * @param currentDay the current day string used by the strategy
     * @return the performance score
     */
    @Override
    public double calculatePerformanceScore(final Developer dev, final String currentDay) {
        List<Ticket> closedTicketsLastMonth = dev.getClosedTicketsFromLastMonth(currentDay);
        double bonusPoints = SENIOR.getBonusPoints();
        int closedTickets = closedTicketsLastMonth.size();
        int highPriorityTickets =
                PerformanceScoreStrategy.getHighPriorityTickets(closedTicketsLastMonth);
        double avgResolutionTime =
                PerformanceScoreStrategy.averageResolutionTime(closedTicketsLastMonth);
        return Math.round(ONE_HUNDRED * (bonusPoints + Math.max(0,
                POINT_FIVE * closedTickets + 1.0 *
                        highPriorityTickets - POINT_FIVE * avgResolutionTime)))
                / ONE_HUNDRED;
    }
}
