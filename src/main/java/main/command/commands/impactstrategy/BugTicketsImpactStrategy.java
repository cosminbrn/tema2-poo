package main.command.commands.impactstrategy;

import main.tickets.BugTicket;
import main.tickets.FeatureRequestTicket;

import java.util.ArrayList;
import java.util.List;

public class BugTicketsImpactStrategy implements ImpactStrategy {
    public double calculateImpact(List<BugTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (BugTicket bug : tickets) {
            double frequency = bug.getFrequency().getValue();
            double severity = bug.getSeverity().getValue();
            double businessPriority = bug.getBusinessPriority().getValue();

            double baseScore = frequency * severity * businessPriority;
            scores.add(calculateImpactFinal(baseScore, 48.0));
        }
        return calculateAverageImpact(scores);
    }
}
