package main.globals;

/**
 * Observer interface for implementing the Observer design pattern.
 */
public interface Observer {
    /**
     * Update method to notify the observer of changes.
     * @param notification the notification message
     */
    void update(String notification);
}


