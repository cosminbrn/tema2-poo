package main.command.commands.generate.impact.impactstrategy;

import java.util.List;

/**
 * Strategy interface for calculating impact scores for ticket lists.
 */
public interface ImpactStrategy {
    double MAX_VALUE = 100.0;
    /**
     * Calculate the final impact score based on a base score and a maximum value.
     * @param baseScore the computed base score
     * @param maxValue the maximum value used for normalization
     * @return the capped impact percentage (0..100)
     */
    default double calculateImpactFinal(final double baseScore,
                                        final double maxValue) {
        return Math.min(MAX_VALUE, (baseScore * MAX_VALUE) / maxValue);
    }

    /**
     * Calculate the average of multiple impact scores and round to two decimal places.
     * @param scores list of scores
     * @return rounded average
     */
    default double calculateAverageImpact(final List<Double> scores) {
        double res = scores.stream().mapToDouble(Double::doubleValue)
                .average().orElse(0.0);
        return Math.round(res * MAX_VALUE) / MAX_VALUE;
    }
}
