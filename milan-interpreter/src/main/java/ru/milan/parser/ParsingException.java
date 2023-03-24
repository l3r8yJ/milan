package ru.milan.parser;

import java.io.Serial;

/**
 * It's a runtime exception that wraps a checked exception and adds a line number.
 */
public final class ParsingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4683507305190194015L;
    private final int line;

    public ParsingException(final String msg, final Exception cause, final int line) {
        super(msg, cause);
        this.line = line;
    }

    public int line() {
        return this.line;
    }
}
