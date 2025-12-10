package main.command.commands.generate.efficiency.efficiencystrategy;

import main.tickets.UIFeedbackTicket;

import java.util.ArrayList;
import java.util.List;

/**
 * Efficiency strategy for UI feedback tickets.
 */
public class FeedbackTicketsEfficiencyStrategy implements EfficiencyStrategy<UIFeedbackTicket> {
    private static final double MAX_VALUE = 20.0;
    /**
     * Calculate the efficiency score for a list of UI feedback tickets.
     * @param tickets list of UI feedback tickets
     * @return the efficiency score
     */
    @Override
    public double calculateEfficiency(final List<UIFeedbackTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (UIFeedbackTicket feedbackTicket : tickets) {
            double usabilityScore = feedbackTicket.getUsabilityScore();
            double businessValue = feedbackTicket.getBusinessValue().getValue();
            int resolutionTime = feedbackTicket.getResolutionTime();
            double baseScore = (usabilityScore + businessValue) / resolutionTime;

            scores.add(calculateImpactFinal(baseScore, MAX_VALUE));
        }
        return calculateAverageImpact(scores);
    }
}

