package main.command.commands.riskstrategy;

import main.tickets.UIFeedbackTicket;

import java.util.ArrayList;
import java.util.List;

public class FeedbackTicketsRiskStrategy implements RiskStrategy {
    public double calculateImpact(List<UIFeedbackTicket> tickets) {
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
