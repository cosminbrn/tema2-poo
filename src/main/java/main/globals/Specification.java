package main.globals;

/**
 * Generic specification interface used for filtering entities.
 * @param <T> the type of object to test
 */
public interface Specification<T> {
    /**
     * Returns true if the given item satisfies the specification.
     * @param item the item to test
     * @return true if the specification is satisfied, false otherwise
     */
    boolean isSatisfiedBy(T item);
}
