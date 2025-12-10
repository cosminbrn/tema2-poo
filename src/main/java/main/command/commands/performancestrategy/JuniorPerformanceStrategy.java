package main.command.commands.performancestrategy;

import main.tickets.Ticket;
import main.users.Developer;

import java.util.List;

import static main.command.commands.performancestrategy.SeniorityBonus.JUNIOR;

public class JuniorPerformanceStrategy implements PerformanceScoreStrategy {
    @Override
    public double calculatePerformanceScore(Developer dev, String currentDay) {
        List<Ticket> closedTicketsLastMonth = dev.getClosedTicketsFromLastMonth(currentDay);
        return Math.round(100.0 * (JUNIOR.getBonusPoints() + Math.max(0,  0.5 * closedTicketsLastMonth.size() - PerformanceScoreStrategy.ticketDiversityFactor(PerformanceScoreStrategy.totalBugTickets(closedTicketsLastMonth),
                PerformanceScoreStrategy.totalFeatureTickets(closedTicketsLastMonth),
                PerformanceScoreStrategy.totalUITickets(closedTicketsLastMonth))))) / 100.0;
    }
}
