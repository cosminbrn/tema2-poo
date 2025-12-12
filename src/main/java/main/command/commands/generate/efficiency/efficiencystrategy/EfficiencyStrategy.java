package main.command.commands.generate.efficiency.efficiencystrategy;

import java.util.List;

/**
 * Strategy interface for calculating efficiency scores for ticket lists.
 * @param <T> the ticket type the strategy operates on
 */
public interface EfficiencyStrategy<T> {
    double OH = 100.0;
    /**
     * Calculate the final impact score based on a base score and a maximum
     * allowed value.
     * @param baseScore the computed base score
     * @param maxValue  the maximum value used for normalization
     * @return the capped impact percentage (0..100)
     */
    default double calculateImpactFinal(final double baseScore, final double maxValue) {
        return Math.min(OH, (baseScore * OH) / maxValue);
    }

    /**
     * Calculate the average of multiple impact scores and round to two
     * decimal places.
     * @param scores list of scores
     * @return rounded average
     */
    default double calculateAverageImpact(final List<Double> scores) {
        double res = scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return Math.round(res * OH) / OH;
    }

    /**
     * Calculate the efficiency score for a list of tickets.
     * @param tickets the list of tickets to evaluate
     * @return the efficiency score
     */
    double calculateEfficiency(List<T> tickets);
}
