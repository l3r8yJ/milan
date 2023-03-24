package ru.milan.interpreter.message;

import lombok.RequiredArgsConstructor;
import org.cactoos.Text;
import org.cactoos.text.FormattedText;

/**
 * It's a `Text` that formats an error message.
 */
@RequiredArgsConstructor
public final class FormattedErrorMessage implements Text {

    private final int line;
    private final int pos;
    private final String message;

    @Override
    public String asString() {
        return new FormattedText(
            "Error at [%d, %d]: %s\n",
            this.line,
            this.pos,
            this.message
        ).toString();
    }
}
