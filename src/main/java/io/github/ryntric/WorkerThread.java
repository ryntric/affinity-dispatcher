package io.github.ryntric;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

final class WorkerThread<T> extends Thread {
    private final Consumer<T> handler;
    private final Channel<T> channel;
    private final int batchsize;
    private final AtomicBoolean isRunning;

    public WorkerThread(String name, ThreadGroup group, int batchsize, Channel<T> channel, Consumer<T> handler) {
        super(group, name);
        this.batchsize = batchsize;
        this.channel = channel;
        this.handler = handler;
        this.isRunning = new AtomicBoolean(false);
    }

    public Channel<T> getChannel() {
        return channel;
    }

    @Override
    public void start() {
        if (isRunning.compareAndSet(false, true)) {
            super.start();
        }
    }

    @Override
    public void run() {
        while (isRunning.getAcquire()) {
            channel.receive(batchsize, handler);
        }
    }

    public void terminate() {
        isRunning.setRelease(false);
        channel.wakeupConsumer();
    }

}
