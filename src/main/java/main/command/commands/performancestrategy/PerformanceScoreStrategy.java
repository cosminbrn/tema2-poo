package main.command.commands.performancestrategy;

import main.tickets.Ticket;
import main.users.Developer;

import java.util.List;

import static main.globals.TicketType.BUG;
import static main.globals.TicketType.FEATURE_REQUEST;
import static main.globals.TicketType.UI_FEEDBACK;
import static main.tickets.enums.BusinessPriority.CRITICAL;
import static main.tickets.enums.BusinessPriority.HIGH;

/**
 * Strategy interface used to calculate a developer's performance score.
 */
public interface PerformanceScoreStrategy {
    /**
     * Constant used to divide by three when averaging three values.
     */
    double DIVIDE_BY_THREE = 3.0;

    /**
     * Constant used when converting to a percentage and rounding.
     */
    double PERCENT_MULTIPLIER = 100.0;

    /**
     * Calculate the performance score for the provided developer at the
     * specified current day.
     *
     * @param dev        the developer to evaluate
     * @param currentDay the current day string used by strategies
     * @return the performance score
     */
    double calculatePerformanceScore(final Developer dev, final String currentDay);

    /**
     * Compute the average number of resolved tickets across three types.
     * @param bug resolved bug count
     * @param feature resolved feature count
     * @param ui resolved UI feedback count
     * @return the average (rounded as double)
     */
    static double averageResolvedTicketType(final int bug, final int feature,
                                            final int ui) {
        return (bug + feature + ui) / DIVIDE_BY_THREE;
    }

    /**
     * Compute the standard deviation across three integer values.
     * @param bug first value
     * @param feature second value
     * @param ui third value
     * @return the standard deviation
     */
    static double standardDeviation(final int bug, final int feature,
                                    final int ui) {
        double mean = averageResolvedTicketType(bug, feature, ui);
        double variance = (
                Math.pow(bug - mean, 2)
                        + Math.pow(feature - mean, 2)
                        + Math.pow(ui - mean, 2)
        ) / DIVIDE_BY_THREE;
        return Math.sqrt(variance);
    }

    /**
     * Calculate ticket diversity factor (std / mean) for the three ticket
     * types. Returns 0.0 when there are no tickets.
     * @param bug resolved bug count
     * @param feature resolved feature count
     * @param ui resolved UI feedback count
     * @return diversity factor
     */
    static double ticketDiversityFactor(final int bug, final int feature,
                                        final int ui) {
        double mean = averageResolvedTicketType(bug, feature, ui);

        if (mean == 0.0) {
            return 0.0;
        }

        double std = standardDeviation(bug, feature, ui);
        return std / mean;
    }

    /**
     * Count the number of bug tickets in the provided list.
     * @param tickets list of tickets
     * @return bug ticket count
     */
    static int totalBugTickets(final List<Ticket> tickets) {
        int count = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getType() == BUG) {
                count++;
            }
        }
        return count;
    }

    /**
     * Count the number of feature request tickets in the provided list.
     * @param tickets list of tickets
     * @return feature request ticket count
     */
    static int totalFeatureTickets(final List<Ticket> tickets) {
        int count = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getType() == FEATURE_REQUEST) {
                count++;
            }
        }
        return count;
    }

    /**
     * Count the number of UI feedback tickets in the provided list.
     * @param tickets list of tickets
     * @return UI feedback ticket count
     */
    static int totalUITickets(final List<Ticket> tickets) {
        int count = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getType() == UI_FEEDBACK) {
                count++;
            }
        }
        return count;
    }

    /**
     * Compute the average resolution time across the provided tickets and
     * round to two decimal places.
     * @param tickets list of tickets
     * @return rounded average resolution time
     */
    static double averageResolutionTime(final List<Ticket> tickets) {
        if (tickets.isEmpty()) {
            return 0.0;
        }

        double totalTime = 0;
        for (Ticket ticket : tickets) {
            totalTime += ticket.getResolutionTime();
        }

        return Math.round(PERCENT_MULTIPLIER * (totalTime / tickets.size())) /
                PERCENT_MULTIPLIER;
    }

    /**
     * Count tickets whose previous priority is HIGH or CRITICAL.
     * @param tickets list of tickets
     * @return count of high/critical previous-priority tickets
     */
    static int getHighPriorityTickets(final List<Ticket> tickets) {
        int count = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getPreviousBusinessPriority() == HIGH
                    || ticket.getPreviousBusinessPriority() == CRITICAL) {
                count++;
            }
        }
        return count;
    }
}
