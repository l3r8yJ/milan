package ru.milan.interpreter.exception;

import ru.milan.interpreter.message.FormattedErrorMessage;

public class InterpretationException extends RuntimeException {

    private final int line;
    private final int pos;

    public InterpretationException(
        final int line,
        final int pos,
        final String msg,
        final Throwable cause
    ) {
        super(msg, cause);
        this.line = line;
        this.pos = pos;
    }

    @Override
    public final String getMessage() {
        return new FormattedErrorMessage(
            this.line,
            this.pos,
            super.getMessage()
        ).asString();
    }
}
