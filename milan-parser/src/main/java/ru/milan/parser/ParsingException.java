package ru.milan.parser;

public class ParsingException extends RuntimeException {

    private final int line;

    public ParsingException(final String msg, final Exception cause, final int line) {
        super(msg, cause);
        this.line = line;
    }

    public int line() {
        return this.line;
    }
}
