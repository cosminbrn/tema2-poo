package main.command.commands.impactstrategy;

import main.tickets.BugTicket;
import main.tickets.FeatureRequestTicket;

import java.util.ArrayList;
import java.util.List;

/**
 * Impact strategy for bug tickets.
 */
public class BugTicketsImpactStrategy implements ImpactStrategy {
    /**
     * Calculate the impact score for a list of bug tickets.
     * @param tickets list of bug tickets
     * @return the impact score
     */
    public double calculateImpact(final List<BugTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (BugTicket bug : tickets) {
            double frequency = bug.getFrequency().getValue();
            double severity = bug.getSeverity().getValue();
            double businessPriority = bug.getBusinessPriority().getValue();

            double baseScore = frequency * severity * businessPriority;
            scores.add(calculateImpactFinal(baseScore, 48.0));
        }
        return calculateAverageImpact(scores);
    }
}
