package io.github.ryntric;

public interface Handler<T> {
    void handle(String workerName, T value);
}
