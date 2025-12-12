package main.command.commands.generate.risk.riskstrategy;

import main.tickets.BugTicket;

import java.util.ArrayList;
import java.util.List;

/**
 * Risk calculation strategy for lists of bug tickets.
 * Subclasses overriding {@link #calculateImpact(List)} should preserve
 * the contract of returning a non-negative impact value.
 */
public class BugTicketsRiskStrategy implements RiskStrategy {
    private static final double MAX_VALUE = 12.0;

    /**
     * Calculates the overall impact for the given bug tickets.
     *
     * @param tickets the list of bug tickets
     * @return the aggregated impact score for the tickets
     */
    public double calculateImpact(final List<BugTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (BugTicket bug : tickets) {
            double frequency = bug.getFrequency().getValue();
            double severity = bug.getSeverity().getValue();

            double baseScore = frequency * severity;
            scores.add(calculateImpactFinal(baseScore, MAX_VALUE));
        }
        return calculateAverageImpact(scores);
    }
}
