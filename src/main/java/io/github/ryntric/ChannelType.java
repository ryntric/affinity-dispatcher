package io.github.ryntric;

/**
 * Defines the types of channels supported by the system.
 * {@code SPSC} — Single-Producer, Single-Consumer channel.
 * {@code MPSC} — Multi-Producer, Single-Consumer channel.
 * These types determine the internal concurrency model and
 * behavior of the channel when multiple threads are producing or consuming messages.
 */
public enum ChannelType {
    SPSC, MPSC
}
