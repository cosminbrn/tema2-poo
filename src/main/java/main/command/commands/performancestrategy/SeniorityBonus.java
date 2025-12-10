package main.command.commands.performancestrategy;

import lombok.Getter;

public enum SeniorityBonus {
    JUNIOR(5),
    MID(15),
    SENIOR(25);

    @Getter
    private final int bonusPoints;

    SeniorityBonus(int bonusPoints) {
        this.bonusPoints = bonusPoints;
    }
}
