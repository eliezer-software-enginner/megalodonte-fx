package org.megalodonte.reactivity.v2;

public interface ReadableState<T> {
    T get();
    void subscribe(java.util.function.Consumer<T> listener);
}