package ru.milan.interpreter;

import java.util.HashMap;
import java.util.Map;

/**
 * @todo #5 Unit tests for AnnotativeMemory.
 * Write unit tests for AnnotativeMemory.
 */
/**
 * The memory, annotation way.
 */
public final class AnnotativeMemory implements Memory<Atom> {

    private final Map<String, Atom> memory = new HashMap<>(0);

    @Override
    public Atom get(final String name) {
        return this.memory.get(name);
    }

    @Override
    public void assign(final String name, final Atom value) {
        this.memory.put(name, value);
    }

    @Override
    public void free() {
        this.memory.clear();
    }
}
