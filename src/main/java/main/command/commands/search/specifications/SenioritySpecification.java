package main.command.commands.search.specifications;

import main.globals.Specification;
import main.globals.userenums.Seniority;
import main.users.Developer;

public final class SenioritySpecification implements Specification<Developer> {
    private final Seniority requiredSeniority;

    public SenioritySpecification(final Seniority seniority) {
        this.requiredSeniority = seniority;
    }

    /**
     * Checks if the developer's seniority matches the required seniority.
     * @param developer the developer to check
     * @return true if the developer's seniority matches, false otherwise
     */
    @Override
    public boolean isSatisfiedBy(final Developer developer) {
        return developer.getSeniority() == this.requiredSeniority;
    }
}
