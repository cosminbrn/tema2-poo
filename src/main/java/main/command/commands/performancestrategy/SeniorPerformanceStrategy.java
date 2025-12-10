package main.command.commands.performancestrategy;

import main.tickets.Ticket;
import main.users.Developer;

import java.util.List;

import static main.command.commands.performancestrategy.SeniorityBonus.SENIOR;

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
    public double calculatePerformanceScore(Developer dev, String currentDay) {
        List<Ticket> closedTicketsLastMonth = dev.getClosedTicketsFromLastMonth(currentDay);
        double bonusPoints = SENIOR.getBonusPoints();
        int closedTickets = closedTicketsLastMonth.size();
        int highPriorityTickets = PerformanceScoreStrategy.getHighPriorityTickets(closedTicketsLastMonth);
        double avgResolutionTime = PerformanceScoreStrategy.averageResolutionTime(closedTicketsLastMonth);
        double value = Math.round(100.0 * (bonusPoints + Math.max(0,  0.5 * closedTickets + 1.0 * highPriorityTickets - 0.5 * avgResolutionTime))) / 100.0;
        return value;
    }
}
