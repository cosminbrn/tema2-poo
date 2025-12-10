package main.command.commands.generate.risk.riskstrategy;

import main.tickets.BugTicket;

import java.util.ArrayList;
import java.util.List;

public class BugTicketsRiskStrategy implements RiskStrategy {
    private static final double MAX_VALUE = 12.0;
    public double calculateImpact(final List<BugTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (BugTicket bug : tickets) {
            double frequency = bug.getFrequency().getValue();
            double severity = bug.getSeverity().getValue();

            double baseScore = frequency * severity;
            scores.add(calculateImpactFinal(baseScore, MAX_VALUE));
        }
        return calculateAverageImpact(scores);
    }
}
