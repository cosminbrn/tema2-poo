package main.command.commands.generate.performance.performancestrategy;

import main.tickets.Ticket;
import main.users.Developer;

import java.util.List;

import static main.command.commands.generate.performance.performancestrategy.SeniorityBonus.JUNIOR;

/**
 * Strategy for calculating performance score for junior developers.
 */
public class JuniorPerformanceStrategy implements PerformanceScoreStrategy {
    private static final double ONE_HUNDRED = 100.0;
    private static final double POINT_FIVE = 0.5;
    /**
     * Calculate the performance score for a junior developer for the given day.
     * @param dev the developer to evaluate
     * @param currentDay the current day string used by the strategy
     * @return the performance score
     */
    @Override
    public double calculatePerformanceScore(final Developer dev, final String currentDay) {
        List<Ticket> closedTicketsLastMonth = dev.getClosedTicketsFromLastMonth(currentDay);
        double diversityFactor = PerformanceScoreStrategy.ticketDiversityFactor(
                PerformanceScoreStrategy.totalBugTickets(closedTicketsLastMonth),
                PerformanceScoreStrategy.totalFeatureTickets(closedTicketsLastMonth),
                PerformanceScoreStrategy.totalUITickets(closedTicketsLastMonth));
        return Math.round(ONE_HUNDRED * (JUNIOR.getBonusPoints() + Math.max(0,
                POINT_FIVE * closedTicketsLastMonth.size() - diversityFactor))) / ONE_HUNDRED;
    }
}
