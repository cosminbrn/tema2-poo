package main.command.commands.generate.risk.riskstrategy;

import main.command.commands.generate.impact.impactstrategy.ImpactStrategy;
import main.tickets.FeatureRequestTicket;

import java.util.ArrayList;
import java.util.List;

public class FeatureRequestTicketsRiskStrategy implements ImpactStrategy {
    private static final double MAX_VALUE = 20.0;
    public double calculateImpact(final List<FeatureRequestTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (FeatureRequestTicket featureRequest : tickets) {
            double businessValue = featureRequest.getBusinessValue().getValue();
            double customerDemand = featureRequest.getCustomerDemand().getValue();
            double baseScore = businessValue + customerDemand;

            scores.add(calculateImpactFinal(baseScore, MAX_VALUE));
        }
        return calculateAverageImpact(scores);
    }
}
