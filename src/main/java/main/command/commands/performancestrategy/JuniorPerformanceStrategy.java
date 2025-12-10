package main.command.commands.performancestrategy;

import main.tickets.Ticket;
import main.users.Developer;

import java.util.List;

import static main.command.commands.performancestrategy.SeniorityBonus.JUNIOR;

/**
 * Strategy for calculating performance score for junior developers.
 */
public class JuniorPerformanceStrategy implements PerformanceScoreStrategy {

    /**
     * Calculate the performance score for a junior developer for the given day.
     * @param dev the developer to evaluate
     * @param currentDay the current day string used by the strategy
     * @return the performance score
     */
    @Override
    public double calculatePerformanceScore(Developer dev, String currentDay) {
        List<Ticket> closedTicketsLastMonth = dev.getClosedTicketsFromLastMonth(currentDay);
        return Math.round(100.0 * (JUNIOR.getBonusPoints() + Math.max(0,  0.5 * closedTicketsLastMonth.size() - PerformanceScoreStrategy.ticketDiversityFactor(PerformanceScoreStrategy.totalBugTickets(closedTicketsLastMonth),
                PerformanceScoreStrategy.totalFeatureTickets(closedTicketsLastMonth),
                PerformanceScoreStrategy.totalUITickets(closedTicketsLastMonth))))) / 100.0;
    }
}
