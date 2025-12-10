package main.command.commands.efficiencystrategy;

import main.tickets.FeatureRequestTicket;
import main.tickets.Ticket;

import java.util.ArrayList;
import java.util.List;

public class FeatureRequestEfficiencyStrategy implements EfficiencyStrategy {
    public double calculateEfficiency(List<FeatureRequestTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (FeatureRequestTicket featureRequest : tickets) {
            double businessValue = featureRequest.getBusinessValue().getValue();
            double customerDemand = featureRequest.getCustomerDemand().getValue();
            double resolutionTime = featureRequest.getResolutionTime();
            double baseScore = (businessValue + customerDemand) / resolutionTime;

            scores.add(calculateImpactFinal(baseScore, 20.0));
        }
        return calculateAverageImpact(scores);
    }
}