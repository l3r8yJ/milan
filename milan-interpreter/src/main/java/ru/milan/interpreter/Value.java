package ru.milan.interpreter;

import java.util.Objects;
import java.util.function.BiFunction;
import org.cactoos.text.FormattedText;
import ru.milan.interpreter.exception.WrongTypeException;

/**
 * It's a wrapper around an object that can be either a number or a string
 */
public final class Value {

    public static final Value FALSE = new Value(0);

    public static final Value TRUE = new Value(1);

    private final Object value;

    private final boolean isNaN;

    public Value(final Integer value) {
        this.value = value;
        this.isNaN = false;
    }

    private Value(final Object value, final boolean isNaN) {
        this.value = value;
        this.isNaN = isNaN;
    }

    /**
     * If the value is an Integer, return true, otherwise return false.
     *
     * @return The value of the instance variable value.
     */
    public boolean isNumber() {
        return this.value instanceof Integer;
    }

    /**
     * Returns true if this is a NaN value, false otherwise.
     *
     * @return The boolean value of the isNaN variable.
     */
    public boolean isNaN() {
        return this.isNaN;
    }

    /**
     * If the value is an Integer, return it, otherwise throw a ClassCastException.
     *
     * @return The value of the integer.
     */
    public Integer internal() {
        return Integer.class.cast(this.value);
    }

    /**
     * "If the value is a number, return true if it's not zero."
     *
     * @return A boolean value.
     */
    public boolean isTrue() {
        this.assertNumber();
        return 0 != this.internal();
    }

    /**
     * "If this is a number, return true if it's zero, otherwise return false."
     *
     * @return A boolean value.
     */
    public boolean isFalse() {
        this.assertNumber();
        return 0 == this.internal();
    }

    /**
     * Logical negative.
     *
     * @return A Value object.
     */
    public Value not() {
        Value result = Value.FALSE;
        this.assertNumber();
        if (0 == this.internal()) {
            result = Value.TRUE;
        }
        return result;
    }

    /**
     * Logical and.
     *
     * @param other The other value to be compared with.
     * @return A Value object.
     */
    public Value and(final Value other) {
        Value result = Value.FALSE;
        if (this.isTrue() && other.isTrue()) {
            result = Value.TRUE;
        }
        return result;
    }

    /**
     * Logical or.
     *
     * @param other The other value to compare with.
     * @return A Value object.
     */
    public Value or(final Value other) {
        Value result = Value.FALSE;
        if (this.isTrue() || other.isTrue()) {
            result = Value.TRUE;
        }
        return result;
    }

    /**
     * Multiply this value by another value.
     *
     * @param other The other value to perform the operation on.
     * @return A new Value object with the result of the operation.
     */
    public Value mul(final Value other) {
        return this.arithmetic(other, (x, y) -> x * y);
    }

    public Value div(final Value other) {
        return this.arithmetic(other, (x, y) -> x / y);
    }

    /**
     * Add two values together, and return the result.
     *
     * @param other The other value to add to this one.
     * @return The result of the arithmetic operation.
     */
    public Value add(final Value other) {
        return this.arithmetic(other, Integer::sum);
    }

    /**
     * Subtracts the value of the other value from this value and returns the
     * result.
     *
     * @param other The value to subtract from this value.
     * @return A new Value object with the result of the operation.
     */
    public Value sub(final Value other) {
        return this.arithmetic(other, (x, y) -> x - y);
    }

    /**
     * Greater than.
     *
     * @param other The other value to compare to.
     * @return A boolean value
     */
    public Value gt(final Value other) {
        return this.boolOperation(other, (x, y) -> x > y);
    }

    /**
     * Greater-equals than.
     *
     * @param other The other value to compare to.
     * @return A boolean value
     */
    public Value gte(final Value other) {
        return this.boolOperation(other, (x, y) -> x >= y);
    }

    /**
     * Less than.
     *
     * @param other The other value to compare to.
     * @return A boolean value
     */
    public Value lt(final Value other) {
        return this.boolOperation(other, (x, y) -> x < y);
    }

    /**
     * Less-equals than.
     *
     * @param other The other value to compare to.
     * @return A boolean value
     */
    public Value lte(final Value other) {
        return this.boolOperation(other, (x, y) -> x <= y);
    }

    /**
     * Equals.
     *
     * @param other The other value to compare to.
     * @return A boolean value.
     */
    public Value eq(final Value other) {
        Value result = Value.FALSE;
        if (this.isNumber() && other.isNumber()) {
            result = this.boolOperation(other, Integer::equals);
        }
        return result;
    }

    /**
     * Not equals.
     *
     * @param other The value to compare to.
     * @return A boolean value.
     */
    public Value neq(final Value other) {
        Value result = Value.FALSE;
        if (this.eq(other).equals(Value.TRUE)) {
            result = Value.TRUE;
        }
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Value)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        final int result;
        if (null != this.value) {
            result = this.value.hashCode();
        } else {
            result = 0;
        }
        return Objects.hash(result, this.isNaN);
    }

    /**
     * "If this value is not a number, throw an exception."
     *
     * The first line of the function is a guard clause. It's a simple if statement
     * that checks if the value is a number. If it is, the function returns
     * immediately. If it isn't, the function continues
     */
    private void assertNumber() {
        if (!this.isNumber()) {
            throw new WrongTypeException(
                new FormattedText(
                    "Couldn't evaluate numeric expression '%s' – not a number",
                    this.value.toString()
                ).toString()
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
    private Value arithmetic(
        final Value other,
        final BiFunction<Integer, Integer, Integer> action
    ) {
        this.assertNumber();
        other.assertNumber();
        return new Value(action.apply(this.internal(), other.internal()));
    }

    /**
     * "If the two values are numbers, and the action returns true, return true,
     * otherwise return false."
     *
     * @param other The other value to compare to.
     * @param action a function that takes two integers and returns a boolean
     * @return A Value object.
     */
    private Value boolOperation(
        final Value other,
        final BiFunction<Integer, Integer, Boolean> action
    ) {
        Value result = Value.FALSE;
        this.assertNumber();
        other.assertNumber();
        if (action.apply(this.internal(), other.internal())) {
            result = Value.TRUE;
        }
        return result;
    }
}
