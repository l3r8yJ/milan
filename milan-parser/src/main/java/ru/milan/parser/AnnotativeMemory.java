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
public final class AnnotativeMemory {

    private final Map<String, Value> memory = new HashMap<>(0);

    /**
     * Get from memory.
     *
     * @param name The name of the variable to get.
     * @return The value of the variable.
     */
    public Value get(final String name) {
        return this.memory.get(name);
    }

    /**
     * Assign in memory.
     *
     * @param name The name of the variable to assign.
     * @param value The value to assign to the variable.
     */
    public void assign(final String name, final Value value) {
        this.memory.put(name, value);
    }

    /**
     * Clear the memory.
     */
    public void free() {
        this.memory.clear();
    }
}
