package main.command.commands.search.specifications;

import main.globals.Specification;

import java.util.List;

public class ConjunctionSpecification<T> implements Specification<T> {
    private final List<Specification<T>> specificationList;

    public ConjunctionSpecification(final List<Specification<T>> specificationList) {
        this.specificationList = specificationList;
    }

    @Override
    public boolean isSatisfiedBy(final T item) {
        for (Specification<T> spec : specificationList) {
            if (!spec.isSatisfiedBy(item)) {
                return false;
            }
        }
        return true;
    }
}
