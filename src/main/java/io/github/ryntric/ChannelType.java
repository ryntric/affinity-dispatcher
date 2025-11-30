package io.github.ryntric;

/**
 * Defines the types of channels supported by the system.
 *
 * <li>{@code SPSC} — Single-Producer, Single-Consumer channel.</li>
 * <li>{@code MPSC} — Multi-Producer, Single-Consumer channel.</li>
 * These types determine the internal concurrency model and
 * behavior of the channel when multiple threads are producing or consuming messages.
 */
public enum ChannelType {
    SPSC, MPSC
}
