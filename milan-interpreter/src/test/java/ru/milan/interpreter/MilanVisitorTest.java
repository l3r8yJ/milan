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
        this.injectBaosAndValue(42);
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
        this.injectBaosAndValue(5);
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
        this.injectBaosAndValue(5);
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
        this.injectBaosAndValue(0);
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
        this.injectBaosAndValue(0);
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
        this.injectBaosAndValue(33);
        final RuleContext mul = MilanVisitorTest.contextFromString("A * 10;");
        if (mul instanceof ProgramParser.MultiplicationContext ctx) {
            MatcherAssert.assertThat(
                "10 * 33 = 3300",
                new MilanVisitor().visitMultiplication(ctx).asInteger(),
                Matchers.equalTo(3300)
            );
        }
    }

    @Test
    void visitsDivision() {
        this.injectBaosAndValue(10);
        final RuleContext div = MilanVisitorTest.contextFromString("A / 2;");
        if (div instanceof ProgramParser.DivisionContext ctx) {
            MatcherAssert.assertThat(
                "10 / 2 = 5",
                new MilanVisitor().visitDivision(ctx).asInteger(),
                Matchers.equalTo(3300)
            );
        }
    }

    @Test
    void visitsAddition() {
        this.injectBaosAndValue(3);
        final RuleContext add = MilanVisitorTest.contextFromString("10 + A;");
        if (add instanceof ProgramParser.AdditionContext ctx) {
            MatcherAssert.assertThat(
                "10 + 3 = 13",
                new MilanVisitor().visitAddition(ctx).asInteger(),
                Matchers.equalTo(13)
            );
        }
    }

    @Test
    void visitsSubs() {
        this.injectBaosAndValue(3);
        final RuleContext sub = MilanVisitorTest.contextFromString("10 - A;");
        if (sub instanceof ProgramParser.SubtractingContext ctx) {
            MatcherAssert.assertThat(
                "10 - 3 = 7",
                new MilanVisitor().visitSubtracting(ctx).asInteger(),
                Matchers.equalTo(7)
            );
        }
    }

    @Test
    void visitsEquals() {
        this.injectBaosAndValue(3);
        final RuleContext eqls =
            MilanVisitorTest.contextFromString("3 == A;");
        if (eqls instanceof ProgramParser.EqualsContext ctx) {
            MatcherAssert.assertThat(
                "3 == 3 is true",
                new MilanVisitor().visitEquals(ctx),
                Matchers.is(Value.TRUE)
            );
        }
    }

    @Test
    void visitsEqualsWithFalse() {
        this.injectBaosAndValue(3);
        final RuleContext eqls =
            MilanVisitorTest.contextFromString("2 == A;");
        if (eqls instanceof ProgramParser.EqualsContext ctx) {
            MatcherAssert.assertThat(
                "2 == 3 is false",
                new MilanVisitor().visitEquals(ctx),
                Matchers.is(Value.FALSE)
            );
        }
    }

    /**
     * Injects the ByteArrayOutputStream and the value into the visitor.
     *
     * @param value The value to be assigned to the variable A.
     */
    private void injectBaosAndValue(final int value) {
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