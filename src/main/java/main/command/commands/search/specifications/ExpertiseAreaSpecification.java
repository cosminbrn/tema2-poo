package main.command.commands.search.specifications;

import main.globals.Specification;
import main.globals.userenums.ExpertiseArea;
import main.users.Developer;

public final class ExpertiseAreaSpecification implements Specification<Developer> {
    private final ExpertiseArea requiredExpertiseArea;

    public ExpertiseAreaSpecification(final ExpertiseArea expertiseArea) {
        this.requiredExpertiseArea = expertiseArea;
    }

    /**
     * Checks if the developer's expertise area matches the required expertise area.
     * @param developer the developer to check
     * @return true if the developer's expertise area matches, false otherwise
     */
    @Override
    public boolean isSatisfiedBy(final Developer developer) {
        return developer.getExpertiseArea() == this.requiredExpertiseArea;
    }
}
