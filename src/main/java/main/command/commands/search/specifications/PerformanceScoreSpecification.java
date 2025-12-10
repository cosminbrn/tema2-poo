package main.command.commands.search.specifications;

import main.globals.Specification;
import main.users.Developer;

public class PerformanceScoreSpecification implements Specification<Developer> {
    private final double referenceScore;
    private final SpecificationFlag referenceFlag;

    public PerformanceScoreSpecification(final double referenceScore,
                                         final SpecificationFlag flag) {
        this.referenceScore = referenceScore;
        this.referenceFlag = flag;
    }
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
