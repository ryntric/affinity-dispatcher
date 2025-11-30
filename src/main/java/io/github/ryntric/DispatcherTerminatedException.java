package io.github.ryntric;

public final class DispatcherTerminatedException extends RuntimeException {
    public DispatcherTerminatedException(String name) {
        super(String.format("Dispatcher [%s] has been terminated", name));
    }
}
