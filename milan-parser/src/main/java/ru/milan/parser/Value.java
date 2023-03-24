package ru.milan.parser;

import org.cactoos.text.FormattedText;
import ru.milan.parser.exception.WrongTypeException;

/**
 * It's a wrapper around an object that can be either a number or a string
 */
public final class Value {

    public static final Value FALSE = new Value(0);

    public static final Value TRUE = new Value(1);

    private final Object value;

    public Value(final Integer value) {
        this.value = value;
    }

    public boolean isNumber() {
        return this.value instanceof Integer;
    }

    public Integer internal() {
        return Integer.class.cast(this.value);
    }

    public boolean isTrue() {
        this.assertNumber();
        return 0 != this.internal();
    }

    public boolean isFalse() {
        this.assertNumber();
        return 0 == this.internal();
    }

    private void assertNumber() {
        if (!this.isNumber()) {
            throw new WrongTypeException(
                new FormattedText(
                    "Couldn't evaluate numeric expression \'%s\' – not a number",
                    this.value.toString()
                ).toString()
            );
        }
    }
}
