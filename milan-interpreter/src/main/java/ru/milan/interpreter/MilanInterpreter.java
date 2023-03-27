package ru.milan.interpreter;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.cactoos.Input;
import org.cactoos.Text;
import org.cactoos.list.ListOf;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Joined;
import org.cactoos.text.Split;
import org.cactoos.text.TextOf;
import ru.milan.interpreter.exception.InterpretationException;
import ru.milan.interpreter.message.FormattedErrorMessage;

/**
 * @todo #5 Unit tests for Interpreter.
 * We have to write unit tests for Interpreter.
 */
/**
 * It reads the input file, creates a lexer and a interpreter, and executes the interpreter
 */
public final class MilanInterpreter {

    private final Memory<Atom> memory;
    private final Input input;

    public MilanInterpreter(final Memory<Atom> memory, final Input input) {
        this.memory = new AnnotativeMemory();
        this.input = input;
    }

    /**
     * It reads the input file,
     * creates a lexer and a interpreter, and executes the interpreter
     */
    public void run() throws IOException {
        final List<Text> lines = this.lines();
        final ANTLRErrorListener errors = new ErrorListener(lines);
        final ProgramLexer lexer = new MilanLexer(this.unixize());
        lexer.removeErrorListeners();
        lexer.addErrorListener(errors);
        final ProgramParser parser = new ProgramParser(
            new CommonTokenStream(lexer)
        );
        parser.setErrorHandler(new BailErrorStrategy());
        parser.removeErrorListeners();
        parser.addErrorListener(errors);
        try (final PrintStream stderr = new PrintStream(System.err, true)) {
            this.execute(parser, stderr);
        }
    }

    /**
     * It walks the parse tree and executes the program
     *
     * @param parser The interpreter that was created by the ANTLR4 runtime.
     * @param stderr The stream to which error messages are written.
     */
    private void execute(final ProgramParser parser, final PrintStream stderr) {
        try {
            this.walkParseTree(parser);
        } catch (final InterpretationException ex) {
            stderr.println(ex.getMessage());
            throw new IllegalStateException(ex);
        } catch (final ParseCancellationException ex) {
            MilanInterpreter.formatIfParseCancelled(stderr, ex);
            throw new IllegalStateException(ex);
        }
    }

    /**
     * It creates a visitor that will visit the parse tree of the program, and then
     * it visits the parse tree
     *
     * @param parser The interpreter object that was created by the ANTLR interpreter
     * generator.
     */
    private void walkParseTree(final ProgramParser parser) {
        final ParseTreeVisitor<Atom> visitor = new MilanVisitor(
            System.in,
            System.out,
            System.err,
            this.memory
        );
        visitor.visit(parser.prog());
    }

    /**
     * If the exception is caused by an input mismatch, print the line number and
     * column number of the offending token
     *
     * @param stderr The stream to write the error message to.
     * @param ex The exception that was thrown.
     */
    private static void formatIfParseCancelled(
        final PrintStream stderr,
        final ParseCancellationException ex
    ) {
        if (ex.getCause() instanceof InputMismatchException cause) {
            stderr.println(
                new FormattedErrorMessage(
                    cause.getOffendingToken().getLine(),
                    cause.getOffendingToken().getCharPositionInLine(),
                    "Syntax error: ".concat(cause.getMessage())
                )
            );
        }
    }

    /**
     * Normalize input to UNIX format.
     * Ensure EOL at EOF.
     *
     * @return UNIX formatted text.
     */
    private Text unixize() {
        return new FormattedText(
            "%s\n",
            new Joined(new TextOf("\n"), this.lines())
        );
    }

    /**
     * "Split the input text into lines, and return the lines as a list of text."
     *
     * @return A list of lines.
     */
    private List<Text> lines() {
        return new ListOf<>(new Split(new TextOf(this.input), "\r?\n"));
    }
}
