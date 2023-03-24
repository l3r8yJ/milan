package ru.milan.parser.exception;

import ru.milan.parser.message.FormattedErrorMessage;

public abstract class InterpretationException extends RuntimeException {

    private final int line;
    private final int pos;

    public InterpretationException(final int line, final int pos, final String msg) {
        super(msg);
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
