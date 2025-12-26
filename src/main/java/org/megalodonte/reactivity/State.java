package org.megalodonte.reactivity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class State<T> {

    private T value;
    private final List<Consumer<T>> listeners = new ArrayList<>();

    public State(T initial) {
        this.value = initial;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
        listeners.forEach(l -> l.accept(value));
    }

    public void subscribe(Consumer<T> listener) {
        listeners.add(listener);
        listener.accept(value); // dispara valor inicial
    }
}
