package main.command.commands.search.specifications;

import main.globals.Specification;

import java.util.List;

/**
 * Specification that is satisfied only when all contained specifications are satisfied.
 * @param <T> type of object being tested
 */
public class ConjunctionSpecification<T> implements Specification<T> {
    private final List<Specification<T>> specificationList;

    public ConjunctionSpecification(final List<Specification<T>> specificationList) {
        this.specificationList = specificationList;
    }

    /**
     * Checks whether the given item satisfies all wrapped specifications.
     * @param item the item being tested
     * @return true if all specifications are satisfied, false otherwise
     */
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
