package main.command.commands.generate.risk.riskstrategy;

import lombok.Getter;

/**
 * Enum representing different risk score levels.
 */
public enum RiskScore {
    NEGLIGIBLE("NEGLIGIBLE", 25),
    MODERATE("MODERATE", 50),
    SIGNIFICANT("SIGNIFICANT", 75),
    MAJOR("MAJOR", 100);

    @Getter
    private final String name;

    @Getter
    private final int upperThreshold;

    RiskScore(final String name, final int upperThreshold) {
        this.name = name;
        this.upperThreshold = upperThreshold;
    }

    public static RiskScore fromInt(final int score) {
        if (score >= 0 && score < NEGLIGIBLE.getUpperThreshold()) {
            return NEGLIGIBLE;
        } else if (score >= NEGLIGIBLE.getUpperThreshold() && score < MODERATE.upperThreshold) {
            return MODERATE;
        } else if (score >= MODERATE.getUpperThreshold() && score < SIGNIFICANT.upperThreshold) {
            return SIGNIFICANT;
        } else if (score >= SIGNIFICANT.getUpperThreshold() && score <= MAJOR.upperThreshold) {
            return MAJOR;
        } else {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
    }
}
