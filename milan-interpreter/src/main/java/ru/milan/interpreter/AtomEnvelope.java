package ru.milan.interpreter;

import java.util.Objects;
import java.util.function.BiFunction;
import org.cactoos.text.FormattedText;
import ru.milan.interpreter.exception.WrongTypeException;

public abstract class AtomEnvelope implements Atom {

    private final Object value;

    private final boolean nan;

    /**
     * Primary ctor,
     *
     * @param value The value.
     * @param nan Is not number.
     */
    protected AtomEnvelope(final Object value, final boolean nan) {
        this.value = value;
        this.nan = nan;
    }

    @Override
    public final boolean isNumber() {
        return this.value instanceof Integer;
    }

    @Override
    public final boolean isNaN() {
        return this.nan;
    }

    @Override
    public final Integer asInteger() {
        return Integer.class.cast(this.value);
    }

    @Override
    public final boolean isTrue() {
        this.assertNumber();
        return 0 != this.asInteger();
    }

    @Override
    public final boolean isFalse() {
        this.assertNumber();
        return 0 == this.asInteger();
    }

    @Override
    public final Atom not() {
        Atom result = Value.FALSE;
        this.assertNumber();
        if (0 == this.asInteger()) {
            result = Value.TRUE;
        }
        return result;
    }

    @Override
    public final Atom and(final Atom other) {
        Atom result = Value.FALSE;
        if (this.isTrue() && other.isTrue()) {
            result = Value.TRUE;
        }
        return result;
    }

    @Override
    public final Atom or(final Atom other) {
        Atom result = Value.FALSE;
        if (this.isTrue() || other.isTrue()) {
            result = Value.TRUE;
        }
        return result;
    }

    @Override
    public final Atom mul(final Atom other) {
        return this.arithmetic(other, (x, y) -> x * y);
    }

    @Override
    public final Atom div(final Atom other) {
        return this.arithmetic(other, (x, y) -> x / y);
    }

    @Override
    public final Atom add(final Atom other) {
        return this.arithmetic(other, Integer::sum);
    }

    @Override
    public final Atom sub(final Atom other) {
        return this.arithmetic(other, (x, y) -> x - y);
    }

    @Override
    public final Atom gt(final Atom other) {
        return this.boolOperation(other, (x, y) -> x > y);
    }

    @Override
    public final Atom gte(final Atom other) {
        return this.boolOperation(other, (x, y) -> x >= y);
    }

    @Override
    public final Atom lt(final Atom other) {
        return this.boolOperation(other, (x, y) -> x < y);
    }

    @Override
    public final Atom lte(final Atom other) {
        return this.boolOperation(other, (x, y) -> x <= y);
    }

    @Override
    public final Atom eq(final Atom other) {
        Atom result = Value.FALSE;
        if (this.isNumber() && other.isNumber()) {
            result = this.boolOperation(other, Integer::equals);
        }
        return result;
    }

    @Override
    public final Atom neq(final Atom other) {
        Atom result = Value.TRUE;
        if (this.eq(other).equals(Value.TRUE)) {
            result = Value.FALSE;
        }
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        boolean result = false;
        if (obj instanceof Atom) {
            result = super.equals(obj);
        }
        return result;
    }

    @Override
    public final int hashCode() {
        final int result;
        if (null != this.value) {
            result = this.value.hashCode();
        } else {
            result = 0;
        }
        return Objects.hash(result, this.nan);
    }

    @Override
    public final void assertNumber() {
        if (!this.isNumber()) {
            throw new WrongTypeException(
                "Couldn't evaluate numeric expression '%s' – not a number"
                    .formatted(this.value)
            );
        }
    }

    /**
     * This function takes a function that takes two integers and returns an
     * integer, and returns a function that takes a Value and returns a Value.
     *
     * @param other The other value to perform the operation on.
     * @param action a function that takes two integers and returns an integer.
     * @return A new Value object.
     */
    private Atom arithmetic(
        final Atom other,
        final BiFunction<Integer, Integer, Integer> action
    ) {
        this.assertNumber();
        other.assertNumber();
        return new Value(action.apply(this.asInteger(), other.asInteger()));
    }

    /**
     * "If the two values are numbers, and the action returns true, return true,
     * otherwise return false."
     *
     * @param other The other value to compare to.
     * @param action a function that takes two integers and returns a boolean
     * @return A Value object.
     */
    private Atom boolOperation(
        final Atom other,
        final BiFunction<Integer, Integer, Boolean> action
    ) {
        Atom result = Value.FALSE;
        this.assertNumber();
        other.assertNumber();
        if (action.apply(this.asInteger(), other.asInteger())) {
            result = Value.TRUE;
        }
        return result;
    }
}
