package main.command.commands.search.specifications;

import main.globals.Specification;
import main.globals.userenums.Seniority;
import main.users.Developer;

public class SenioritySpecification implements Specification<Developer> {
    private final Seniority requiredSeniority;

    public SenioritySpecification(final Seniority seniority) {
        this.requiredSeniority = seniority;
    }

    @Override
    public boolean isSatisfiedBy(final Developer developer) {
        return developer.getSeniority() == this.requiredSeniority;
    }
}
