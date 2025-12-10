package main.command.commands.riskstrategy;

import main.tickets.BugTicket;

import java.util.ArrayList;
import java.util.List;

/**
 * Risk strategy for bug tickets.
 */
public class BugTicketsRiskStrategy implements RiskStrategy {
    /**
     * Calculate the risk impact for a list of bug tickets.
     * @param tickets list of bug tickets
     * @return the impact score
     */
    public double calculateImpact(final List<BugTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (BugTicket bug : tickets) {
            double frequency = bug.getFrequency().getValue();
            double severity = bug.getSeverity().getValue();

            double baseScore = frequency * severity;
            scores.add(calculateImpactFinal(baseScore, 12.0));
        }
        return calculateAverageImpact(scores);
    }
}
