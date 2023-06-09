package ru.milan.interpreter;

import java.util.BitSet;
import java.util.InputMismatchException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.cactoos.Text;

@RequiredArgsConstructor
public final class MilanErrorListener extends BaseErrorListener {

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
        throw new InputMismatchException(
            "[%d:%d] %s: \"%s\""
                .formatted(
                    line, position, msg,
                    this.lines.size() < line ? "EOF" : this.lines.get(line - 1)
                )
        );
    }
}
