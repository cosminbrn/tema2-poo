package main.command.commands.riskstrategy;

import main.tickets.BugTicket;

import java.util.ArrayList;
import java.util.List;

public class BugTicketsRiskStrategy implements RiskStrategy {
    public double calculateImpact(List<BugTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (BugTicket bug : tickets) {
            double frequency = bug.getFrequency().getValue();
            double severity = bug.getSeverity().getValue();

            double baseScore = frequency * severity;
            scores.add(calculateImpactFinal(baseScore, 12.0));
        }
        return calculateAverageImpact(scores);
    }
}
