package io.github.ryntric;

final class Worker<T> {
    private final String name;
    private final Channel<T> channel;
    private final WorkerThread<T> thread;

    Worker(WorkerThread<T> thread) {
        this.name = thread.getName();
        this.channel = thread.getChannel();
        this.thread = thread;
    }

    public final String getName() {
        return name;
    }

    public final long getChannelSize() {
        return channel.size();
    }

    public final void publish(T value) {
        channel.push(value);
    }

    @SafeVarargs
    public final void publish(T... values) {
        channel.push(values);
    }

    public final void start() {
        thread.start();
    }

    public final void terminate() {
        thread.terminate();
    }
}
