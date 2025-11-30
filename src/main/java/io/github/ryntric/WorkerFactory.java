package io.github.ryntric;

final class WorkerFactory<T> {
    private static final String NAME_TEMPLATE = "%s-worker-th-%d";

    private final String name;
    private final int priority;
    private final ThreadGroup group;
    private final int batchsize;
    private final Handler<T> handler;

    private int nextId = 0;

    public WorkerFactory(String name, int priority, int batchsize, Handler<T> handler) {
        this.name = name;
        this.priority = priority;
        this.group = new ThreadGroup(name);
        this.batchsize = batchsize;
        this.handler = handler;
    }

    private String getName(String prefix) {
        return String.format(NAME_TEMPLATE, prefix, nextId++);
    }

    public WorkerThread<T> createWorker(Channel<T> channel) {
        WorkerThread<T> thread = new WorkerThread<>(getName(name), group, batchsize, channel, handler);
        thread.setPriority(priority);
        return thread;
    }
}
