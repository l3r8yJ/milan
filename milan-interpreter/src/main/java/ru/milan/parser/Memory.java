package ru.milan.parser;

/**
 * The runtime memory.
 *
 * @param <T> The type.
 */
public interface Memory<T> {

    /**
     * Get from memory.
     *
     * @param name The name of the variable to get.
     * @return The value of the variable.
     */
    T get(String name);

    /**
     * Assign in memory.
     *
     * @param name The name of the variable to assign.
     * @param value The value to assign to the variable.
     */
    void assign(String name, T value);

    /**
     * Clear the memory.
     */
    void free();
}
