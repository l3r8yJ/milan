package ru.milan.interpreter;

/**
 * It's a wrapper around an object that can be either a number or a string
 */
public final class Value extends AtomEnvelope {

    public static final Value FALSE = new Value(0);

    public static final Value TRUE = new Value(1);

    /**
     * Integer ctor.
     *
     * @param value Integer value
     */
    public Value(final Integer value) {
        super(value, false);
    }
}
