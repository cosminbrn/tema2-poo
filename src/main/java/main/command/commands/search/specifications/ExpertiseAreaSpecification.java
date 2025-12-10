package main.command.commands.search.specifications;

import main.globals.Specification;
import main.globals.userenums.ExpertiseArea;
import main.users.Developer;

public class ExpertiseAreaSpecification implements Specification<Developer> {
    private final ExpertiseArea requiredExpertiseArea;

    public ExpertiseAreaSpecification(final ExpertiseArea expertiseArea) {
        this.requiredExpertiseArea = expertiseArea;
    }

    @Override
    public boolean isSatisfiedBy(final Developer developer) {
        return developer.getExpertiseArea() == this.requiredExpertiseArea;
    }
}
