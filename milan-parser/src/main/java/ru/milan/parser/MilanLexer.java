package ru.milan.parser;

import java.io.IOException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.cactoos.Text;
import org.cactoos.io.InputStreamOf;

public class MilanLexer extends ProgramLexer {

    public MilanLexer(final Text text) throws IOException {
        this(CharStreams.fromStream(new InputStreamOf(text)));
    }

    public MilanLexer(final CharStream input) {
        super(input);
    }
}
