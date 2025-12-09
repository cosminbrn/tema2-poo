package main.globals;

public interface Observbale {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(String notification);
}