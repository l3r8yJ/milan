package ru.milan.interpreter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import lombok.SneakyThrows;
import org.antlr.v4.runtime.CommonTokenStream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.milan.interpreter.exception.AtomNotDefinedException;
import ru.milan.interpreter.fake.FakeLexer;

/**
 * @todo #28:60m/DEV Test cases While statement.
 * Unit tests for while statement.
 */
/**
 * @todo #28:60m/DEV Test cases Visit brackets.
 * Unit tests for brackets statement.
 */
/**
 * @todo #28:60m/DEV Test cases expressions.
 * Unit tests for common expressions.
 */
/**
 * Test case for {@link MilanVisitor}.
 */
final class MilanVisitorTest {

    private ProgramVisitor<Atom> visitor;
    private final Memory<Atom> memory = new AnnotativeMemory();

    @BeforeEach
    void setUp() {
        this.visitor = new MilanVisitor(
            System.in,
            System.out,
            System.err,
            new AnnotativeMemory()
        );
    }

    @AfterEach
    void tearDown() {
        this.memory.free();
    }

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
        System.setOut(new PrintStream(out));
        this.visitor = new MilanVisitor();
        this.visitor.visit(
            MilanVisitorTest.parser("output.mil").outputStmt()
        );
        MatcherAssert.assertThat(
            "Write right output",
            out.toString(),
            Matchers.equalTo("101\n")
        );
        Assertions.assertThrows(
            AtomNotDefinedException.class,
            () ->
                this.visitor.visit(
                    MilanVisitorTest.parser("output_a.mil").outputStmt()
                ),
            "Atom {A} is not defined"
        );
    }

    @Test
    void visitsOutputMemorized() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        this.memory.assign("A", new Value(42));
        this.visitor = new MilanVisitor(System.out, this.memory);
        this.visitor.visit(
            MilanVisitorTest.parser("output_a.mil").outputStmt()
        );
        MatcherAssert.assertThat(
            "Write right output",
            out.toString(),
            Matchers.equalTo("42\n")
        );
    }

    @Test
    void visitsIncrement() {
        this.memory.assign("A", new Value(10));
        this.visitor = new MilanVisitor(this.memory);
        final Atom pre = this.visitor.visit(
            MilanVisitorTest.parser("preincrement.mil").incrStmt()
        );
        MatcherAssert.assertThat(
            "pre-incremented value from 10",
            pre.asInteger(),
            Matchers.equalTo(11)
        );
        final ProgramParser parsed =
            MilanVisitorTest.parser("postincrement.mil");
        MatcherAssert.assertThat(
            "post-incremented value from 11",
            this.visitor.visit(parsed.incrStmt()).asInteger(),
            Matchers.equalTo(12)
        );
    }

    @Test
    void visitsSimpleIfStatement() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        this.memory.assign("A", new Value(5));
        this.visitor = new MilanVisitor(System.out, this.memory);
        this.visitor.visit(
            MilanVisitorTest.parser("if_simple.mil").ifStmt()
        );
        MatcherAssert.assertThat(
            "Output from IF body is 555",
            baos.toString(),
            Matchers.equalTo("555\n")
        );
    }

    @Test
    void visitsSimpleIfWithElseStatement() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        this.memory.assign("A", new Value(5));
        this.visitor = new MilanVisitor(System.out, this.memory);
        this.visitor.visit(
            MilanVisitorTest.parser("if_else.mil").ifStmt()
        );
        MatcherAssert.assertThat(
            "Output from ELSE body",
            baos.toString(),
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