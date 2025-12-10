package main.command.commands.impactstrategy;

import main.tickets.FeatureRequestTicket;

import java.util.ArrayList;
import java.util.List;

/**
 * Impact strategy for feature request tickets.
 */
public class FeatureRequestTicketsImpactStrategy implements ImpactStrategy {
    /**
     * Calculate the impact score for a list of feature request tickets.
     * @param tickets list of feature request tickets
     * @return the impact score
     */
    public double calculateImpact(final List<FeatureRequestTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (FeatureRequestTicket featureRequest : tickets) {
            double businessValue = featureRequest.getBusinessValue().getValue();
            double customerDemand = featureRequest.getCustomerDemand().getValue();
            double baseScore = businessValue * customerDemand;

            scores.add(calculateImpactFinal(baseScore, 100.0));
        }
        return calculateAverageImpact(scores);
    }
}
