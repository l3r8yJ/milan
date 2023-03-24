package ru.milan.interpreter;

import java.util.Objects;
import java.util.function.BiFunction;
import org.cactoos.text.FormattedText;
import ru.milan.interpreter.exception.WrongTypeException;

/**
 * It's a wrapper around an object that can be either a number or a string
 */
public final class Value extends AtomEnvelope {

    public static final Value FALSE = new Value(0);

    public static final Value TRUE = new Value(1);

    public Value(final Integer value) {
        super(value, false);
    }
}
