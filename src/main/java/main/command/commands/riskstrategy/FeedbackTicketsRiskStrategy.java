package main.command.commands.riskstrategy;

import main.tickets.UIFeedbackTicket;

import java.util.ArrayList;
import java.util.List;

/**
 * Risk strategy for UI feedback tickets.
 */
public class FeedbackTicketsRiskStrategy implements RiskStrategy {
    /**
     * Calculate the risk impact for a list of UI feedback tickets.
     * @param tickets list of UI feedback tickets
     * @return the impact score
     */
    public double calculateImpact(final List<UIFeedbackTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (UIFeedbackTicket bug : tickets) {
            double businessValue = bug.getBusinessValue().getValue();
            double usabilityScore = bug.getUsabilityScore();
            double baseScore = (11.0 - usabilityScore) * businessValue;
            scores.add(calculateImpactFinal(baseScore, 100.0));
        }
        return calculateAverageImpact(scores);
    }
}
