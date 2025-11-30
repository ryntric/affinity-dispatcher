package io.github.ryntric;

/**
 * Factory class for creating {@link Channel} instances.
 * Provides a simple static method to create channels of type SPSC (single-producer, single-consumer)
 * or MPSC (multi-producer, single-consumer) with the desired buffer size and wait strategies for producers and consumers.
 */
final class ChannelFactory {
    private ChannelFactory() {
    }

    /**
     * Creates a {@link Channel} of the specified type, size, and wait strategies.
     *
     * @param <T>                      the type of messages stored in the channel
     * @param type                     the channel type (SPSC or MPSC)
     * @param size                     the buffer size of the channel
     * @param producerWaitStrategyType the wait strategy for the producer
     * @param consumerWaitStrategyType the wait strategy for the consumer
     * @return a new {@link Channel} instance
     */
    public static <T> Channel<T> createChannel(ChannelType type, int size, ProducerWaitStrategyType producerWaitStrategyType, ConsumerWaitStrategyType consumerWaitStrategyType) {
        Channel<T> channel = null;
        switch (type) {
            case SPSC: {
                channel = Channel.spsc(size, producerWaitStrategyType, consumerWaitStrategyType);
                break;
            }
            case MPSC: {
                channel = Channel.mpsc(size, producerWaitStrategyType, consumerWaitStrategyType);
                break;
            }
        }
        return channel;
    }

}
