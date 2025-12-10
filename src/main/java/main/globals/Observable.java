package main.globals;

/**
 * Observable interface for implementing the Observer design pattern.
 */
public interface Observable {
    /**
     * Adds an observer to the list of observers.
     * @param observer the observer to add
     */
    void addObserver(Observer observer);

    /**
     * Removes an observer from the list of observers.
     * @param observer the observer to remove
     */
    void removeObserver(Observer observer);

    /**
     * Notifies all observers of a change.
     * @param notification the notification message
     */
    void notifyObservers(String notification);
}