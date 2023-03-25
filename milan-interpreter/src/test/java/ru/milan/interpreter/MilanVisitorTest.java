package ru.milan.interpreter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import lombok.SneakyThrows;
import org.antlr.v4.runtime.CommonTokenStream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.milan.interpreter.fake.FakeLexer;

/**
 * Test case for {@link MilanVisitor}.
 */
final class MilanVisitorTest {

    private ProgramVisitor<Atom> visitor;

    @BeforeEach
    void setUp() {
        this.visitor = new MilanVisitor(
            System.in,
            System.out,
            System.err,
            new AnnotativeMemory()
        );
    }

    /**
     * @todo #18:60m/DEV Find a proper way to test it.
     * Turn on Disabled test and find way to test it
     */
    @Test
    void visitsAssign() {
        final Integer atom = this.visitor.visit(
            MilanVisitorTest.parser("assign.mil").assignStmt()
        ).asInteger();
        MatcherAssert.assertThat(
            "Read right assign from file",
            321,
            Matchers.equalTo(atom)
        );
    }

    @Test
    void visitsOutput() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.visitor = new MilanVisitor(
            System.in,
            new PrintStream(out),
            System.err,
            new AnnotativeMemory()
        );
        this.visitor.visit(
            MilanVisitorTest.parser("output.mil").outputStmt()
        );
        MatcherAssert.assertThat(
            "Read right output",
            out.toString(),
            Matchers.equalTo("101\n")
        );
    }

    /**
     * It creates a parser for a given resource
     *
     * @param resource the name of the resource file to parse
     * @return A ProgramParser object.
     */
    @SneakyThrows
    private static ProgramParser parser(final String resource) {
        return new ProgramParser(new CommonTokenStream(new FakeLexer(resource)));
    }
}