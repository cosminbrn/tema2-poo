package main.command.commands.search.specifications;

import main.globals.Specification;
import main.users.Developer;

public final class PerformanceScoreSpecification implements Specification<Developer> {
    private final double referenceScore;
    private final SpecificationFlag referenceFlag;

    public PerformanceScoreSpecification(final double referenceScore,
                                         final SpecificationFlag flag) {
        this.referenceScore = referenceScore;
        this.referenceFlag = flag;
    }
    /**
     * Checks if the developer's performance score satisfies the specification.
     * @param dev the developer to check
     * @return true if the developer's performance score satisfies, false otherwise
     */
    @Override
    public boolean isSatisfiedBy(final Developer dev) {
        if (referenceFlag == SpecificationFlag.ABOVE) {
            return dev.getPerformanceScore() > referenceScore;
        } else if (referenceFlag == SpecificationFlag.BELOW) {
            return dev.getPerformanceScore() < referenceScore;
        }
        return true;
    }
}
