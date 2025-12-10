package main.command.commands.performancestrategy;

import lombok.Getter;

/**
 * Enum holding bonus points for developer seniority levels.
 */
public enum SeniorityBonus {
    JUNIOR(5),
    MID(15),
    SENIOR(30);

    @Getter
    private final int bonusPoints;

    /**
     * Construct a SeniorityBonus value with provided points.
     * @param bonusPoints the bonus points for this seniority level
     */
    SeniorityBonus(int bonusPoints) {
        this.bonusPoints = bonusPoints;
    }
}
