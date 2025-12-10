package main.command.commands.performancestrategy;

import main.tickets.Ticket;
import main.tickets.enums.BusinessPriority;
import main.users.Developer;

import java.util.List;

import static main.globals.TicketType.*;
import static main.tickets.enums.BusinessPriority.CRITICAL;
import static main.tickets.enums.BusinessPriority.HIGH;

public interface PerformanceScoreStrategy {
    double calculatePerformanceScore(Developer dev, String currentDay);

    static double averageResolvedTicketType(int bug, int feature, int ui) {
        return (bug + feature + ui) / 3.0;
    }

    static double standardDeviation(int bug, int feature, int ui) {
        double mean = averageResolvedTicketType(bug, feature, ui);
        double variance = (Math.pow(bug - mean, 2) + Math.pow(feature - mean, 2) + Math.pow(ui - mean, 2)) / 3.0;
        return Math.sqrt(variance);
    }

    static double ticketDiversityFactor(int bug, int feature, int ui) {
        double mean = averageResolvedTicketType(bug, feature, ui);

        // dacă nu există tichete, diversitatea este 0
        if (mean == 0.0) {
            return 0.0;
        }

        double std = standardDeviation(bug, feature, ui);
        return std / mean;
    }

    static int totalBugTickets(List<Ticket> tickets) {
        int count = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getType() == BUG) {
                count++;
            }
        }
        return count;
    }

    static int totalFeatureTickets(List<Ticket> tickets) {
        int count = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getType() == FEATURE_REQUEST) {
                count++;
            }
        }
        return count;
    }

    static int totalUITickets(List<Ticket> tickets) {
        int count = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getType() == UI_FEEDBACK) {
                count++;
            }
        }
        return count;
    }

    static double averageResolutionTime(List<Ticket> tickets) {
        if (tickets.isEmpty()) {
            return 0;
        }

        double totalTime = 0;
        for (Ticket ticket : tickets) {
            totalTime += ticket.getResolutionTime();
        }

        return Math.round(100.0 * (totalTime / tickets.size())) / 100.0;
    }

    static int getHighPriorityTickets(List<Ticket> tickets) {
        int count = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getBusinessPriority() == HIGH || ticket.getBusinessPriority() == CRITICAL) {
                count++;
            }
        }
        return count;
    }
}
