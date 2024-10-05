package ku.cs.services;

public interface Observer<T> {
    void update(T data);
}
