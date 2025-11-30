package io.github.ryntric;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AffinityDispatcher is a high-performance dispatcher that routes messages between workers based on a key's hash code.
 * It uses a multiply-high scaling algorithm to map hash codes to routing nodes in a branchless and uniform way.
 * It is suitable for low-latency message dispatching and ensures consistent routing for the same key.
 *
 */
@SuppressWarnings("unchecked")
public final class AffinityDispatcher<T> {
    private static final int NON_STARTED_STATE = 0;
    private static final int STARTED_STATE = 1;
    private static final int TERMINATED_STATE = 2;

    private final String name;
    private final Worker<T>[] workers;
    private final RoutingNode<T>[] routingTable;
    private final HashCodeProvider hashCodeProvider;
    private final int workerCount;
    private final int nodesPerWorker;
    private final long routingTableSize;
    private final AtomicInteger state;


    /**
     * Constructs a new AffinityDispatcher with the given parameters.
     *
     * @param name             the name of the dispatcher
     * @param handler          the data handler for each worker
     * @param hashCodeProvider provider to compute hash codes from keys
     * @param config           dispatcher configuration (worker count, buffer size, etc.)
     */
    public AffinityDispatcher(String name, Handler<T> handler, HashCodeProvider hashCodeProvider, Config config) {
        this.name = name;
        this.workerCount = config.getWorkerCount();
        this.nodesPerWorker = config.getRoutingNodePerWorker();
        this.routingTableSize = (long) workerCount * nodesPerWorker;
        this.hashCodeProvider = hashCodeProvider;
        this.workers = new Worker[workerCount];
        this.routingTable = new RoutingNode[(int) routingTableSize];
        this.state = new AtomicInteger(NON_STARTED_STATE);
        this.init(name, handler, config);
    }

    private void init(String name, Handler<T> handler, Config config) {
        WorkerFactory<T> workerFactory = new WorkerFactory<>(name, config.getWorkerThreadPriority(), config.getBatchSize(), handler);

        for (int i = 0; i < workerCount; i++) {
            Channel<T> channel = ChannelFactory.createChannel(
                    config.getChannelType(),
                    config.getBufferSize(),
                    config.getProducerWaitStrategyType(),
                    config.getConsumerWaitStrategyType()
            );
            workers[i] = new Worker<>(workerFactory.createWorker(channel));
        }

        for (int nodeIdx = 0; nodeIdx < nodesPerWorker; nodeIdx++) {
            for (int workerIdx = 0; workerIdx < workerCount; workerIdx++) {
                routingTable[nodeIdx * workerCount + workerIdx] = new RoutingNode<>(workers[workerIdx]);
            }
        }
    }

    private void checkState() {
        if (state.getAcquire() != STARTED_STATE) {
            throw new DispatcherTerminatedException(name);
        }
    }

    /**
     * Calculates the routing table index for a given hash code using a multiply-high scaling algorithm.
     *
     * @param hashcode the hash code of the key
     * @return the index into the routing table
     */
    private int calculateIndex(int hashcode) {
        long absolute = hashcode & 0xFFFFFFFFL;
        return (int) ((absolute * routingTableSize) >>> 32);
    }

    /**
     * Publishes a message to the routing node corresponding to the given hash code.
     *
     * @param hashcode the hash code of the key
     * @param value    the message to publish
     */
    private void internalDispatch(int hashcode, T value) {
        int index = calculateIndex(hashcode);
        RoutingNode<T> node = routingTable[index];
        node.publish(value);
    }

    /**
     * Dispatches a message using a {@code byte[]} key.
     *
     * @param key   the key used for routing
     * @param value the message to dispatch
     * @throws DispatcherTerminatedException if the dispatcher is not started
     */
    public void dispatch(byte[] key, T value) {
        checkState();
        internalDispatch(hashCodeProvider.provide(key), value);
    }

    /**
     * Dispatches a message using a {@code String} key.
     *
     * @param key   the key used for routing
     * @param value the message to dispatch
     * @throws DispatcherTerminatedException if the dispatcher is not started
     */
    public void dispatch(String key, T value) {
        checkState();
        internalDispatch(hashCodeProvider.provide(key), value);
    }

    /**
     * Dispatches a message using an {@code int} key.
     *
     * @param key   the key used for routing
     * @param value the message to dispatch
     * @throws DispatcherTerminatedException if the dispatcher is not started
     */
    public void dispatch(int key, T value) {
        checkState();
        internalDispatch(hashCodeProvider.provide(key), value);
    }

    /**
     * Dispatches a message using a {@code long} key.
     *
     * @param key   the key used for routing
     * @param value the message to dispatch
     * @throws DispatcherTerminatedException if the dispatcher is not started
     */
    public void dispatch(long key, T value) {
        checkState();
        internalDispatch(hashCodeProvider.provide(key), value);
    }

    /**
     * Starts all workers of the dispatcher. Transitions the dispatcher state to STARTED.
     * This method must be called before dispatching messages.
     */
    public void start() {
        if (state.compareAndSet(NON_STARTED_STATE, STARTED_STATE)) {
            for (Worker<T> worker : workers) worker.start();
        }
    }

    /**
     * Shuts down all workers and transitions the dispatcher state to TERMINATED.
     */
    public void shutdown() {
        if (state.compareAndSet(STARTED_STATE, TERMINATED_STATE)) {
            for (Worker<T> worker : workers) worker.terminate();
        }
    }

    /**
     * Returns the name of the dispatcher.
     *
     * @return the dispatcher name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the number of workers.
     *
     * @return worker count
     */
    public int getWorkerCount() {
        return workerCount;
    }

    /**
     * Returns the total routing table size.
     *
     * @return routing table size
     */
    public int getRoutingTableSize() {
        return (int) routingTableSize;
    }

    /**
     * Returns the number of routing nodes per worker.
     *
     * @return nodes per worker
     */
    public int getNodesPerWorker() {
        return nodesPerWorker;
    }

    /**
     * Returns the sizes of each worker's internal channel.
     *
     * @return a map from worker name to channel size
     */
    public Map<String, Long> getChannelSizes() {
        Map<String, Long> result = new HashMap<>(workerCount);
        for (Worker<T> worker : workers) {
            result.put(worker.getName(), worker.getChannelSize());
        }
        return result;
    }

}
