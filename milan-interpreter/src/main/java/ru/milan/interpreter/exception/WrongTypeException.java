package ru.milan.interpreter.exception;

import java.io.Serial;

public final class WrongTypeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8611745140496379648L;

    public WrongTypeException(final String message) {
        super(message);
    }

    public WrongTypeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
