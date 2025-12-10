package main.command.commands.riskstrategy;

import lombok.Getter;

public enum RiskScore {
    NEGLIGIBLE("NEGLIGIBLE"),
    MODERATE("MODERATE"),
    SIGNIFICANT("SIGNIFICANT"),
    MAJOR("MAJOR");

    @Getter
    private final String name;

    RiskScore(String name) {
        this.name = name;
    }

    public static RiskScore fromInt(int score) {
        if (score >= 0 && score < 25) {
            return NEGLIGIBLE;
        } else if (score >= 25 && score < 50) {
            return MODERATE;
        } else if (score >= 50 && score < 75) {
            return SIGNIFICANT;
        } else if (score >= 75 && score <= 100) {
            return MAJOR;
        } else {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
    }
}
