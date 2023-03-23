package ru.milan.parser;

import java.io.IOException;
import java.util.List;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.cactoos.Input;
import org.cactoos.Output;
import org.cactoos.Text;
import org.cactoos.list.ListOf;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Joined;
import org.cactoos.text.Split;
import org.cactoos.text.TextOf;

public final class Syntax {

    private final String name;

    private final Input input;

    private final Output target;

    public Syntax(final String name, final Input input, final Output target) {
        this.name = name;
        this.input = input;
        this.target = target;
    }

    public void parse() throws IOException {
        final List<Text> lines = this.lines();
        final Lexer lexer = new MilanLexer(this.unixize());
        lexer.removeErrorListeners();
        final ANTLRErrorListener errors = new BaseErrorListener() {
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
                        lines.size() < line ? "EOF" : lines.get(line - 1)
                    ),
                    error,
                    line
                );
            }
        };
        lexer.addErrorListener(errors);
    }

    private Text unixize() {
        return new FormattedText(
            "%s\n",
            new Joined(new TextOf("\n"), this.lines())
        );
    }

    private List<Text> lines() {
        return new ListOf<>(new Split(new TextOf(this.input), "\r?\n"));
    }
}
