package de.mewel.pixellogic.util;

/**
 * A merging FIFO queue. The entries are merged on a key and so the queue interface
 * is not appropriate. Note that the FIFO applies to the keys, not the values.
 *
 * @author nitsanw
 *
 * @param <KEY>
 * @param <VAL>
 */
public interface MergingQueue<KEY, VAL> {
    /**
     * Removed the tail entry of the queue and returns it.
     *
     * @return the latest value for the oldest key in the queue, or null if queue is empty.
     */
    VAL poll();

    /**
     * Inserts a new entry to the queue. If a value for this key already exists then the
     * original order is preserved while the value is replaced.
     *
     * @param key not null
     * @param val not null
     */
    void offer(KEY key, VAL val);

    /**
     * @return true if queue is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Clears the queue of all entries
     */
    void clear();
}
