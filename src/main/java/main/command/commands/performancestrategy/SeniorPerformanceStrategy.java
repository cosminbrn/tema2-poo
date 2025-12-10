package main.command.commands.performancestrategy;

import main.tickets.Ticket;
import main.users.Developer;

import java.util.List;

import static main.command.commands.performancestrategy.SeniorityBonus.SENIOR;

public class SeniorPerformanceStrategy implements PerformanceScoreStrategy {
    @Override
    public double calculatePerformanceScore(Developer dev, String currentDay) {
        List<Ticket> closedTicketsLastMonth = dev.getClosedTicketsFromLastMonth(currentDay);
        return Math.round(100.0 * (SENIOR.getBonusPoints() + Math.max(0,  0.5 * closedTicketsLastMonth.size() + 1.0 * PerformanceScoreStrategy.getHighPriorityTickets(closedTicketsLastMonth) - 0.5 * PerformanceScoreStrategy.averageResolutionTime(closedTicketsLastMonth)))) / 100.0;
    }
}
