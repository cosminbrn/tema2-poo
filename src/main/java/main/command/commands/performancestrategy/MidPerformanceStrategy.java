package main.command.commands.performancestrategy;

import main.tickets.Ticket;
import main.users.Developer;

import java.util.List;

import static main.command.commands.performancestrategy.SeniorityBonus.MID;

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
    public double calculatePerformanceScore(Developer dev, String currentDay) {
        List<Ticket> closedTicketsLastMonth = dev.getClosedTicketsFromLastMonth(currentDay);
        double avg = PerformanceScoreStrategy.averageResolutionTime(closedTicketsLastMonth);
        double value = Math.round(100.0 * (MID.getBonusPoints() + Math.max(0, 0.5 * closedTicketsLastMonth.size() + 0.7 * PerformanceScoreStrategy.getHighPriorityTickets(closedTicketsLastMonth) - 0.3 * avg))) / 100.0;
        return value;
    }
}
