package io.github.ryntric;

final class RoutingNode<T> {
    private final Worker<T> worker;

    public RoutingNode(Worker<T> worker) {
        this.worker = worker;
    }

    public final void publish(T value) {
        worker.publish(value);
    }

    @SafeVarargs
    public final void publish(T... values) {
        worker.publish(values);
    }
}
