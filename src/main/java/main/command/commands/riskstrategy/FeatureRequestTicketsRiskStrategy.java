package main.command.commands.riskstrategy;

import main.command.commands.impactstrategy.ImpactStrategy;
import main.tickets.FeatureRequestTicket;

import java.util.ArrayList;
import java.util.List;

public class FeatureRequestTicketsRiskStrategy implements ImpactStrategy {
    public double calculateImpact(List<FeatureRequestTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (FeatureRequestTicket featureRequest : tickets) {
            double businessValue = featureRequest.getBusinessValue().getValue();
            double customerDemand = featureRequest.getCustomerDemand().getValue();
            double baseScore = businessValue + customerDemand;

            scores.add(calculateImpactFinal(baseScore, 20.0));
        }
        return calculateAverageImpact(scores);
    }
}
