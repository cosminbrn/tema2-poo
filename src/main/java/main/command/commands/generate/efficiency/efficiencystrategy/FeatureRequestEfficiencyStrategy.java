package main.command.commands.generate.efficiency.efficiencystrategy;

import main.tickets.FeatureRequestTicket;

import java.util.ArrayList;
import java.util.List;

/**
 * Efficiency strategy for feature request tickets.
 */
public class FeatureRequestEfficiencyStrategy implements EfficiencyStrategy<FeatureRequestTicket> {
    private static final double MAX_VALUE = 20.0;
    /**
     * Calculate the efficiency score for a list of feature request tickets.
     * @param tickets list of feature request tickets
     * @return the efficiency score
     */
    @Override
    public double calculateEfficiency(final List<FeatureRequestTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (FeatureRequestTicket featureRequest : tickets) {
            double businessValue = featureRequest.getBusinessValue().getValue();
            double customerDemand = featureRequest.getCustomerDemand().getValue();
            double resolutionTime = featureRequest.getResolutionTime();
            double baseScore = (businessValue + customerDemand) / resolutionTime;

            scores.add(calculateImpactFinal(baseScore, MAX_VALUE));
        }
        return calculateAverageImpact(scores);
    }
}
