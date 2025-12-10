package main.command.commands.efficiencystrategy;

import main.tickets.FeatureRequestTicket;
import main.tickets.Ticket;
import main.tickets.UIFeedbackTicket;

import java.util.ArrayList;
import java.util.List;

public class FeedbackTicketsEfficiencyStrategy implements EfficiencyStrategy {
    public double calculateEfficiency(List<UIFeedbackTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (UIFeedbackTicket feedbackTicket : tickets) {
            double usabilityScore = feedbackTicket.getUsabilityScore();
            double businessValue = feedbackTicket.getBusinessValue().getValue();
            int resolutionTime = feedbackTicket.getResolutionTime();
            double baseScore = (usabilityScore + businessValue) / resolutionTime;

            scores.add(calculateImpactFinal(baseScore, 20.0));
        }
        return calculateAverageImpact(scores);
    }
}
