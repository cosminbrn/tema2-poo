package main.command.commands.impactstrategy;

import java.util.List;

public interface ImpactStrategy {
    default double calculateImpactFinal(double baseScore, double maxValue) {
        return Math.min(100.0, (baseScore * 100.0) / maxValue);
    }

    default double calculateAverageImpact(List<Double> scores) {
        return scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
