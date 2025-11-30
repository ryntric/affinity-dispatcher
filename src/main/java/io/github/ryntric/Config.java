package io.github.ryntric;


/**
 * Configuration class for the dispatcher system.
 * Provides configurable parameters such as worker count, buffer size, routing nodes per worker,
 * batch size, channel type, and producer/consumer wait strategies.
 * Use {@link #builder()} to create a {@link Builder} for convenient configuration.
 */
public final class Config {
    /**
     * Number of worker threads (default: number of processors)
     */
    private int workerCount = Utils.getProcessorCount();
    /**
     * Number of routing nodes per worker (default: 400)
     */
    private int routingNodePerWorker = 400;
    /**
     * Size of the internal channel buffer (default: 4096)
     */
    private int bufferSize = 4096;
    /**
     * Batch size for processing messages (default: 2048)
     */
    private int batchSize = 2048;
    /**
     * Worker thread priority (default: Thread.NORM_PRIORITY)
     */
    private int workerThreadPriority = Thread.NORM_PRIORITY;
    /**
     * Channel type for message passing (default: SPSC)
     */
    private ChannelType channelType = ChannelType.SPSC;
    /**
     * Producer wait strategy type (default: SPINNING)
     */
    private ProducerWaitStrategyType producerWaitStrategyType = ProducerWaitStrategyType.SPINNING;
    /**
     * Consumer wait strategy type (default: BLOCKING)
     */
    private ConsumerWaitStrategyType consumerWaitStrategyType = ConsumerWaitStrategyType.BLOCKING;

    private Config() {
    }

    /**
     * Returns a new {@link Builder} instance for configuring a {@link Config} object.
     *
     * @return a new Builder
     */
    public static Builder builder() {
        return new Config().new Builder();
    }

    /**
     * Returns the configured number of worker threads.
     *
     * @return worker thread count
     */
    public int getWorkerCount() {
        return workerCount;
    }

    /**
     * Returns the configured number of routing nodes per worker.
     *
     * @return routing nodes per worker
     */
    public int getRoutingNodePerWorker() {
        return routingNodePerWorker;
    }

    /**
     * Returns the configured channel buffer size.
     *
     * @return buffer size
     */
    public int getBufferSize() {
        return bufferSize;
    }

    /**
     * Returns the configured batch size for message processing. *
     *
     * @return batch size
     */
    public int getBatchSize() {
        return batchSize;
    }

    /**
     * Returns the configured worker thread priority.
     *
     * @return thread priority
     */
    public int getWorkerThreadPriority() {
        return workerThreadPriority;
    }

    /**
     * Returns the configured channel type. *
     *
     * @return channel type
     */
    public ChannelType getChannelType() {
        return channelType;
    }

    /**
     * Returns the configured producer wait strategy type.
     *
     * @return producer wait strategy type
     */
    public ProducerWaitStrategyType getProducerWaitStrategyType() {
        return producerWaitStrategyType;
    }

    /**
     * Returns the configured consumer wait strategy type.
     *
     * @return consumer wait strategy type
     */
    public ConsumerWaitStrategyType getConsumerWaitStrategyType() {
        return consumerWaitStrategyType;
    }

    /**
     * Builder for {@link Config}.
     * Allows fluent configuration of dispatcher parameters.
     */
    public final class Builder {

        /**
         * Sets the number of worker threads.
         *
         * @param workerCount number of workers
         * @return the builder
         */
        public Builder setWorkerCount(int workerCount) {
            Config.this.workerCount = workerCount;
            return this;
        }

        /**
         * Sets the number of routing nodes per worker.
         *
         * @param routingNodePerWorker number of nodes per worker
         * @return the builder
         */
        public Builder setRoutingNodePerWorker(int routingNodePerWorker) {
            Config.this.routingNodePerWorker = routingNodePerWorker;
            return this;
        }

        /**
         * Sets the internal channel buffer size.
         *
         * @param bufferSize size of the buffer
         * @return the builder
         */
        public Builder setBufferSize(int bufferSize) {
            Config.this.bufferSize = bufferSize;
            return this;
        }

        /**
         * Sets the batch size for message processing.
         *
         * @param batchSize batch size
         * @return the builder
         */
        public Builder setBatchSize(int batchSize) {
            Config.this.batchSize = batchSize;
            return this;
        }

        /**
         * Sets the worker thread priority.
         *
         * @param workerThreadPriority thread priority
         * @return the builder
         */
        public Builder setWorkerThreadPriority(int workerThreadPriority) {
            Config.this.workerThreadPriority = workerThreadPriority;
            return this;
        }

        /**
         * Sets the channel type.
         *
         * @param channelType type of channel (SPSC or MPSC)
         * @return the builder
         */
        public Builder setChannelType(ChannelType channelType) {
            Config.this.channelType = channelType;
            return this;
        }

        /**
         * Sets the producer wait strategy type.
         *
         * @param producerWaitStrategyType producer wait strategy
         * @return the builder
         */
        public Builder setProducerWaitStrategyType(ProducerWaitStrategyType producerWaitStrategyType) {
            Config.this.producerWaitStrategyType = producerWaitStrategyType;
            return this;
        }

        /**
         * Sets the consumer wait strategy type.
         *
         * @param consumerWaitStrategyType consumer wait strategy
         * @return the builder
         */
        public Builder setConsumerWaitStrategyType(ConsumerWaitStrategyType consumerWaitStrategyType) {
            Config.this.consumerWaitStrategyType = consumerWaitStrategyType;
            return this;
        }

        /**
         * Builds and returns the configured {@link Config} instance.
         *
         * @return the configured Config object
         */
        public Config build() {
            return Config.this;
        }

    }
}
