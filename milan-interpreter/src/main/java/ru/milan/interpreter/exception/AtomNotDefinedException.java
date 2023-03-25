package ru.milan.interpreter.exception;

import java.io.Serial;
import org.cactoos.Text;

public class AtomNotDefinedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -8835732774840137937L;

    public AtomNotDefinedException(final Text text) {
        super(text.toString());
    }
}
