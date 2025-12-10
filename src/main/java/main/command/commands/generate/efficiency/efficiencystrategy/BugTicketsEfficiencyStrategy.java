package main.command.commands.generate.efficiency.efficiencystrategy;

import main.tickets.BugTicket;

import java.util.ArrayList;
import java.util.List;

/**
 * Efficiency strategy for bug tickets.
 */
public class BugTicketsEfficiencyStrategy implements EfficiencyStrategy<BugTicket> {
    private static final double MAX_VALUE = 70.0;
    private static final double MULTI = 10.0;
    /**
     * Calculate the efficiency score for a list of bug tickets.
     * @param tickets list of bug tickets
     * @return the efficiency score
     */
    @Override
    public double calculateEfficiency(final List<BugTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (BugTicket bug : tickets) {
            int resolutionTime = bug.getResolutionTime();
            int severity = bug.getSeverity().getValue();
            int frequency = bug.getFrequency().getValue();
            double baseScore = (frequency + severity) * MULTI / (resolutionTime);

            scores.add(calculateImpactFinal(baseScore, MAX_VALUE));
        }
        return calculateAverageImpact(scores);
    }
}
