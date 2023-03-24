package ru.milan.parser.exception;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.cactoos.Text;
import ru.milan.parser.ParsingException;

@RequiredArgsConstructor
public final class ErrorListener extends BaseErrorListener {

    private final List<Text> lines;

    @Override
    public void syntaxError(
        final Recognizer<?, ?> recognizer,
        final Object symbol,
        final int line,
        final int position,
        final String msg,
        final RecognitionException error
    ) {
        throw new ParsingException(
            String.format(
                "[%d:%d] %s: \"%s\"",
                line, position, msg,
                this.lines.size() < line ? "EOF" : this.lines.get(line - 1)
            ),
            error,
            line
        );
    }

}
