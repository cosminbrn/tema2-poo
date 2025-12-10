package main.command.commands.efficiencystrategy;

import main.tickets.BugTicket;
import main.tickets.Ticket;

import java.util.ArrayList;
import java.util.List;

public class BugTicketsEfficiencyStrategy implements EfficiencyStrategy {
    public double calculateEfficiency(List<BugTicket> tickets) {
        List<Double> scores = new ArrayList<>();
        for (BugTicket bug : tickets) {
            int resolutionTime = bug.getResolutionTime();
            int severity = bug.getSeverity().getValue();
            int frequency = bug.getFrequency().getValue();
            double baseScore = (frequency + severity) * 10.0 / (resolutionTime);

            scores.add(calculateImpactFinal(baseScore, 70.0));
        }
        return calculateAverageImpact(scores);
    }
}