package main.command.commands.riskstrategy;

import java.util.List;

/**
 * Strategy interface for calculating risk impact scores for ticket lists.
 */
public interface RiskStrategy {
    /**
     * Calculate the final impact score based on a base score and a maximum value.
     * @param baseScore the computed base score
     * @param maxValue the maximum value used for normalization
     * @return the capped impact percentage (0..100)
     */
    default double calculateImpactFinal(double baseScore, double maxValue) {
        return Math.min(100.0, (baseScore * 100.0) / maxValue);
    }

    /**
     * Calculate the average of multiple impact scores and round to two decimal places.
     * @param scores list of scores
     * @return rounded average
     */
    default double calculateAverageImpact(List<Double> scores) {
        double res = scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return Math.round(res * 100.0) / 100.0;
    }
}
