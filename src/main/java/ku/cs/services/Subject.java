package ku.cs.services;

public interface Subject<T> {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void clearObservers();
    void notifyObservers(T data);
}
