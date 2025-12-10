package main.command.commands.impactstrategy;

import main.tickets.BugTicket;
import main.tickets.UIFeedbackTicket;

import java.util.ArrayList;
import java.util.List;

public class FeedbackTicketsImpactStrategy implements ImpactStrategy {
    public double calculateImpact(List<UIFeedbackTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (UIFeedbackTicket bug : tickets) {
            double businessValue = bug.getBusinessValue().getValue();
            double usabilityScore = bug.getUsabilityScore();
            double baseScore = businessValue * usabilityScore;
            scores.add(calculateImpactFinal(baseScore, 100.0));
        }
        return calculateAverageImpact(scores);
    }
}
