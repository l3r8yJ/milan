package ru.milan.interpreter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import lombok.SneakyThrows;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.milan.interpreter.exception.AtomNotDefinedException;
import ru.milan.interpreter.fake.FakeLexer;

/**
 * @todo #28:60m/DEV Test cases Visit brackets.
 * Unit tests for brackets statement.
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
            MilanVisitorTest.parserFromSource("assign.mil").assignStmt()
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
            MilanVisitorTest.parserFromSource("output.mil").outputStmt()
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
                    MilanVisitorTest.parserFromSource("output_a.mil").outputStmt()
                ),
            "Atom {A} is not defined"
        );
    }

    @Test
    void visitsOutputMemorized() {
        this.injectAtomAndBaos(42);
        this.visitor.visit(
            MilanVisitorTest.parserFromSource("output_a.mil").outputStmt()
        );
        MatcherAssert.assertThat(
            "Write right output",
            this.baos.toString(),
            Matchers.equalTo("42\n")
        );
    }

    @Test
    void visitsIncrement() {
        this.fillAndInjectMemoryToVisitor(10, new MilanVisitor(this.memory));
        final Atom pre = this.visitor.visit(
            MilanVisitorTest.parserFromSource("preincrement.mil").incrStmt()
        );
        MatcherAssert.assertThat(
            "pre-incremented value from 10",
            pre.asInteger(),
            Matchers.equalTo(11)
        );
        final ProgramParser parsed =
            MilanVisitorTest.parserFromSource("postincrement.mil");
        MatcherAssert.assertThat(
            "post-incremented value from 11",
            this.visitor.visit(parsed.incrStmt()).asInteger(),
            Matchers.equalTo(12)
        );
    }

    @Test
    void visitsSimpleIfStatement() {
        this.injectAtomAndBaos(5);
        this.visitor.visit(
            MilanVisitorTest.parserFromSource("if_simple.mil").ifStmt()
        );
        MatcherAssert.assertThat(
            "Output from IF body is 555",
            this.baos.toString(),
            Matchers.equalTo("555\n")
        );
    }

    @Test
    void visitsSimpleIfElseStatement() {
        this.injectAtomAndBaos(5);
        this.visitor.visit(
            MilanVisitorTest.parserFromSource("if_else.mil").ifStmt()
        );
        MatcherAssert.assertThat(
            "Output from ELSE body",
            this.baos.toString(),
            Matchers.equalTo("101\n")
        );
    }

    @Test
    void visitsSimpleWhileStatement() {
        this.injectAtomAndBaos(0);
        this.visitor.visit(
            MilanVisitorTest.parserFromSource("simple_while.mil").whileStmt()
        );
        MatcherAssert.assertThat(
            "Outputs 5 times",
            this.baos.toString(),
            Matchers.equalTo("0\n1\n2\n3\n4\n")
        );
    }

    @Test
    void visitsNotSimpleWhileStatement() {
        this.injectAtomAndBaos(0);
        this.visitor.visit(
            MilanVisitorTest.parserFromSource("not_simple_while.mil").whileStmt()
        );
        MatcherAssert.assertThat(
            "Outputs 15 times",
            this.baos.toString(),
            Matchers.equalTo(
                "1\n2\n2\n3\n3\n3\n4\n4\n4\n4\n5\n5\n5\n5\n5\n"
            )
        );
    }

    @Test
    void visitsMultiplication() {
        this.injectAtomAndBaos(33);
        final RuleContext mul = MilanVisitorTest.contextFromString("A * 10;");
        MatcherAssert.assertThat(
            "10 * 33 = 330",
            this.visitor.visit(mul).asInteger(),
            Matchers.equalTo(330)
        );
    }

    @Test
    void visitsDivision() {
        this.injectAtomAndBaos(10);
        final RuleContext div = MilanVisitorTest.contextFromString("A / 2;");
        MatcherAssert.assertThat(
            "10 / 2 = 5",
            this.visitor.visit(div).asInteger(),
            Matchers.equalTo(5)
        );
    }

    @Test
    void visitsAddition() {
        this.injectAtomAndBaos(3);
        final RuleContext add = MilanVisitorTest.contextFromString("10 + A;");
        MatcherAssert.assertThat(
            "10 + 3 = 13",
            this.visitor.visit(add).asInteger(),
            Matchers.equalTo(13)
        );
    }

    @Test
    void visitsSubs() {
        this.injectAtomAndBaos(3);
        final RuleContext sub = MilanVisitorTest.contextFromString("10 - A;");
        MatcherAssert.assertThat(
            "10 - 3 = 7",
            this.visitor.visit(sub).asInteger(),
            Matchers.equalTo(7)
        );
    }

    @Test
    void visitsEquals() {
        this.injectAtomAndBaos(3);
        final RuleContext equals =
            MilanVisitorTest.contextFromString("3 == A;");
        MatcherAssert.assertThat(
            "3 == 3 is true",
            this.visitor.visit(equals),
            Matchers.is(Value.TRUE)
        );
    }

    @Test
    void visitsEqualsWithFalse() {
        this.injectAtomAndBaos(3);
        final RuleContext equals =
            MilanVisitorTest.contextFromString("2 == A;");
        MatcherAssert.assertThat(
            "2 == 3 is false",
            this.visitor.visit(equals),
            Matchers.is(Value.FALSE)
        );
    }

    @Test
    void visitsGreaterThan() {
        this.injectAtomAndBaos(12);
        final RuleContext greaterContext =
            MilanVisitorTest.contextFromString("2 > A;");
        MatcherAssert.assertThat(
            "2 > 12 is false",
            this.visitor.visit(greaterContext),
            Matchers.is(Value.FALSE)
        );
        final RuleContext greaterEqualsContext =
            MilanVisitorTest.contextFromString("12 >= A");
        MatcherAssert.assertThat(
            "12 >= 12 is true",
            this.visitor.visit(greaterEqualsContext),
            Matchers.is(Value.TRUE)
        );
    }

    @Test
    void visitsLessThan() {
        this.injectAtomAndBaos(10);
        final RuleContext lessContext =
            MilanVisitorTest.contextFromString("5 < A");
        MatcherAssert.assertThat(
            "5 < 10 is true",
            this.visitor.visit(lessContext),
            Matchers.equalTo(Value.TRUE)
        );
        final RuleContext lessEqualsContext =
            MilanVisitorTest.contextFromString("5 <= A");
        MatcherAssert.assertThat(
            "5 <= 10 is true",
            this.visitor.visit(lessEqualsContext),
            Matchers.equalTo(Value.TRUE)
        );
    }

    @Test
    void visitsNotEquals() {
        this.injectAtomAndBaos(2);
        final RuleContext notEqualsContext =
            MilanVisitorTest.contextFromString("1 <> A");
        MatcherAssert.assertThat(
            "1 != 2 is true",
            this.visitor.visit(notEqualsContext),
            Matchers.equalTo(Value.TRUE)
        );
        final RuleContext twoNotEqualsTwo =
            MilanVisitorTest.contextFromString("2 <> A");
        MatcherAssert.assertThat(
            "2 != 2 is false",
            this.visitor.visit(twoNotEqualsTwo),
            Matchers.equalTo(Value.FALSE)
        );
    }

    @Test
    void visitsBrackets() {
        this.injectAtomAndBaos(10);
        final Integer result = this.visitor.visit(
            MilanVisitorTest.contextFromString("(A + 5) * (A - 8) / (1 + 1)")
        ).asInteger();
        MatcherAssert.assertThat(
            "15 * 2 = 30",
            result,
            Matchers.equalTo(15)
        );
    }

    /**
     * Injects the ByteArrayOutputStream and the value into the visitor.
     *
     * @param value The value to be assigned to the variable A.
     */
    private void injectAtomAndBaos(final int value) {
        this.injectBaosIntoSystemOut();
        this.fillAndInjectMemoryToVisitor(value, new MilanVisitor(System.out, this.memory));
    }

    /**
     * This function fills the memory with a value and injects the visitor into the
     * memory.
     *
     * @param value The value to be assigned to the memory cell A.
     * @param vis The visitor that will be injected with the memory.
     */
    private void fillAndInjectMemoryToVisitor(final int value, final ProgramVisitor<Atom> vis) {
        this.memory.assign("A", new Value(value));
        this.visitor = vis;
    }

    /**
     * Injects the ByteArrayOutputStream into the System.out stream.
     */
    private void injectBaosIntoSystemOut() {
        System.setOut(new PrintStream(this.baos));
    }

    /**
     * It creates a parser for a given resource
     *
     * @param resource the name of the resource file to parse
     * @return A ProgramParser object.
     */
    @SneakyThrows
    private static ProgramParser parserFromSource(final String resource) {
        return new ProgramParser(new CommonTokenStream(new FakeLexer(resource)));
    }

    /**
     * It takes a string of code, and returns a parser that can parse that code
     *
     * @param code The code to parse
     * @return A ProgramParser object.
     */
    @SneakyThrows
    private static RuleContext contextFromString(final String code) {
        return new ProgramParser(
            new CommonTokenStream(
                new MilanLexer(CharStreams.fromString(code))
            )
        ).expressions().getRuleContext();
    }
}
