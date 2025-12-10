package main.globals;

/**
 * Interface representing a specification.
 * @param <T> Type of the item to be checked.
 */
public interface Specification<T> {
    boolean isSatisfiedBy(T item);
}
