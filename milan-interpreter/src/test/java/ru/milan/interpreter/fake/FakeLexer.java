package ru.milan.interpreter.fake;

import org.antlr.v4.runtime.CharStreams;
import org.cactoos.io.ResourceOf;
import ru.l3r8y.UnixizedOf;
import ru.milan.interpreter.MilanLexer;

public class FakeLexer extends MilanLexer {

    public FakeLexer(final String resource) throws Exception {
        super(CharStreams.fromString(
            new UnixizedOf(new ResourceOf(resource))
                .asText()
                .asString()
            )
        );
    }
}
