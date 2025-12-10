package main.command.commands.efficiencystrategy;

import main.tickets.Ticket;

import java.util.List;

public interface EfficiencyStrategy {
    default double calculateImpactFinal(double baseScore, double maxValue) {
        return Math.min(100.0, (baseScore * 100.0) / maxValue);
    }

    default double calculateAverageImpact(List<Double> scores) {
        double res = scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return Math.round(res * 100.0) / 100.0;
    }
}
