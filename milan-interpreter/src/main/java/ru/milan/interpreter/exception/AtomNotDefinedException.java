package ru.milan.interpreter.exception;

import java.io.Serial;

public class AtomNotDefinedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -8835732774840137937L;

    public AtomNotDefinedException(final String text) {
        super(text.toString());
    }
}
