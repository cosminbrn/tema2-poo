package main.command.commands.generate.risk.riskstrategy;

import main.tickets.UIFeedbackTicket;

import java.util.ArrayList;
import java.util.List;

public class FeedbackTicketsRiskStrategy implements RiskStrategy {
    private static final double MAX_VALUE = 100.0;
    private static final double ELEVEN = 11;
    public double calculateImpact(final List<UIFeedbackTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (UIFeedbackTicket bug : tickets) {
            double businessValue = bug.getBusinessValue().getValue();
            double usabilityScore = bug.getUsabilityScore();
            double baseScore = (ELEVEN - usabilityScore) * businessValue;
            scores.add(calculateImpactFinal(baseScore, MAX_VALUE));
        }
        return calculateAverageImpact(scores);
    }
}
