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
    private ByteArrayOutputStream baos;

    @BeforeEach
    void setUp() {
        this.baos = new ByteArrayOutputStream();
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
        System.setOut(new PrintStream(this.baos));
        this.visitor = new MilanVisitor();
        this.visitor.visit(
            MilanVisitorTest.parser("output.mil").outputStmt()
        );
        MatcherAssert.assertThat(
            "Write right output",
            this.baos.toString(),
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
        this.injectBaosAndValue(42);
        this.visitor.visit(
            MilanVisitorTest.parser("output_a.mil").outputStmt()
        );
        MatcherAssert.assertThat(
            "Write right output",
            this.baos.toString(),
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
        this.injectBaosAndValue(5);
        this.visitor.visit(
            MilanVisitorTest.parser("if_simple.mil").ifStmt()
        );
        MatcherAssert.assertThat(
            "Output from IF body is 555",
            this.baos.toString(),
            Matchers.equalTo("555\n")
        );
    }

    @Test
    void visitsSimpleIfElseStatement() {
        this.injectBaosAndValue(5);
        this.visitor.visit(
            MilanVisitorTest.parser("if_else.mil").ifStmt()
        );
        MatcherAssert.assertThat(
            "Output from ELSE body",
            this.baos.toString(),
            Matchers.equalTo("101\n")
        );
    }

    @Test
    void visitsSimpleWhileStatement() {
        this.injectBaosAndValue(0);
        this.visitor.visit(
            MilanVisitorTest.parser("simple_while.mil").whileStmt()
        );
        MatcherAssert.assertThat(
            "Outputs 5 times",
            this.baos.toString(),
            Matchers.equalTo("0\n1\n2\n3\n4\n")
        );
    }

    @Test
    void visitsNotSimpleWhileStatement() {
        this.injectBaosAndValue(0);
        this.visitor.visit(
            MilanVisitorTest.parser("not_simple_while.mil").whileStmt()
        );
        MatcherAssert.assertThat(
            "Outputs 15 times",
            this.baos.toString(),
            Matchers.equalTo(
                "1\n2\n2\n3\n3\n3\n4\n4\n4\n4\n5\n5\n5\n5\n5\n"
            )
        );
    }

    /**
     * Injects the ByteArrayOutputStream and the value into the visitor.
     *
     * @param value The value to be assigned to the variable A.
     */
    private void injectBaosAndValue(final int value) {
        System.setOut(new PrintStream(this.baos));
        this.memory.assign("A", new Value(value));
        this.visitor = new MilanVisitor(System.out, this.memory);
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