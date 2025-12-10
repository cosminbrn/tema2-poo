package main.command.commands.generate.impact.impactstrategy;

import main.tickets.UIFeedbackTicket;

import java.util.ArrayList;
import java.util.List;

/**
 * Impact strategy for UI feedback tickets.
 */
public class FeedbackTicketsImpactStrategy implements ImpactStrategy {
    private static final double MAX_VALUE = 100.0;
    /**
     * Calculate the impact score for a list of UI feedback tickets.
     * @param tickets list of UI feedback tickets
     * @return the impact score
     */
    public double calculateImpact(final List<UIFeedbackTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (UIFeedbackTicket bug : tickets) {
            double businessValue = bug.getBusinessValue().getValue();
            double usabilityScore = bug.getUsabilityScore();
            double baseScore = businessValue * usabilityScore;
            scores.add(calculateImpactFinal(baseScore, MAX_VALUE));
        }
        return calculateAverageImpact(scores);
    }
}
