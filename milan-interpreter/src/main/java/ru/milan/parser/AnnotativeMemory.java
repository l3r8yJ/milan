package ru.milan.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * @todo #5 Unit tests for AnnotativeMemory.
 * Write unit tests for AnnotativeMemory.
 */
/**
 * The memory, annotation way.
 */
public final class AnnotativeMemory implements Memory<Value>{

    private final Map<String, Value> memory = new HashMap<>(0);

    @Override
    public Value get(final String name) {
        return this.memory.get(name);
    }

    @Override
    public void assign(final String name, final Value value) {
        this.memory.put(name, value);
    }

    @Override
    public void free() {
        this.memory.clear();
    }
}
