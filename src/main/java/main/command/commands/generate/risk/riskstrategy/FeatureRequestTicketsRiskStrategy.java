package main.command.commands.generate.risk.riskstrategy;

import main.command.commands.generate.impact.impactstrategy.ImpactStrategy;
import main.tickets.FeatureRequestTicket;

import java.util.ArrayList;
import java.util.List;

/**
 * Risk calculation strategy for feature request tickets.
 * Subclasses that override {@link #calculateImpact(List)} should
 * preserve the semantics of returning a non-negative impact value.
 */
public class FeatureRequestTicketsRiskStrategy implements ImpactStrategy {
    private static final double MAX_VALUE = 20.0;

    /**
     * Calculates the overall impact for the given feature request tickets.
     * @param tickets the list of feature request tickets
     * @return the aggregated impact score for the tickets
     */
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
