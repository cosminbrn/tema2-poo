package main.command.commands.efficiencystrategy;

import main.tickets.BugTicket;

import java.util.ArrayList;
import java.util.List;

/**
 * Efficiency strategy for bug tickets.
 */
public class BugTicketsEfficiencyStrategy implements EfficiencyStrategy<BugTicket> {
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
            double baseScore = (frequency + severity) * 10.0 / (resolutionTime);

            scores.add(calculateImpactFinal(baseScore, 70.0));
        }
        return calculateAverageImpact(scores);
    }
}