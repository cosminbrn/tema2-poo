package main.users;

import main.globals.ExpertiseArea;
import main.users.enums.Seniority;
import static main.users.enums.Role.DEVELOPER;

/**
 * Class representing a developer user.
 */
public final class Developer extends User {
    private final String hireDate;
    private final ExpertiseArea expertiseArea;
    private final Seniority seniority;

    public Developer(final String name, final String email, final String hireDate,
                         final ExpertiseArea expertiseArea, final Seniority seniority) {
        super(name, email, DEVELOPER);
        this.hireDate = hireDate;
        this.expertiseArea = expertiseArea;
        this.seniority = seniority;
    }
}
