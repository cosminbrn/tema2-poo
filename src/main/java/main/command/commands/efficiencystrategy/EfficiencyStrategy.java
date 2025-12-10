package main.command.commands.efficiencystrategy;

import main.tickets.Ticket;

import java.util.List;

public interface EfficiencyStrategy {
    default double calculateImpactFinal(double baseScore, double maxValue) {
        return Math.min(100.0, (baseScore * 100.0) / maxValue);
    }

    default double calculateAverageImpact(List<Double> scores) {
        return scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
