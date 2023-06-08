package ru.milan.interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.cactoos.Input;
import org.cactoos.Text;
import org.cactoos.list.ListOf;
import org.cactoos.text.Joined;
import org.cactoos.text.Split;
import org.cactoos.text.TextOf;
import ru.l3r8y.UnixizedOf;
import ru.milan.interpreter.exception.InterpretationException;
import ru.milan.interpreter.message.FormattedErrorMessage;

/**
 * It reads the input file, creates a lexer and a interpreter, and executes the interpreter
 */
public final class MilanInterpreter {

    private final Memory<Atom> memory;
    private final Input input;
    private final InputStream stdin;
    private final PrintStream stdout;
    private final PrintStream stderr;

    public MilanInterpreter(
        final Memory<Atom> memory,
        final Input input,
        final InputStream in,
        final PrintStream out,
        final PrintStream err
    ) {
        this.memory = memory;
        this.input = input;
        this.stdin = in;
        this.stdout = new PrintStream(out, true);
        this.stderr = new PrintStream(err, true);
    }

    /**
     * It reads the input file,
     * creates a lexer and a interpreter, and executes the interpreter
     */
    public void run() throws Exception {
        final List<Text> lines = this.lines();
        final ANTLRErrorListener errors = new MilanErrorListener(lines);
        final ProgramLexer lexer = new MilanLexer(new UnixizedOf(this.input).asText());
        lexer.removeErrorListeners();
        lexer.addErrorListener(errors);
        final ProgramParser parser = new ProgramParser(
            new CommonTokenStream(lexer)
        );
        parser.setErrorHandler(new BailErrorStrategy());
        parser.removeErrorListeners();
        parser.addErrorListener(errors);
        this.execute(parser, this.stderr);
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
        } catch (final ParseCancellationException ex) {
            this.formatIfParseCancelled(ex);
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
            this.stdin,
            this.stdout,
            this.stderr,
            this.memory
        );
        visitor.visit(parser.prog());
    }

    /**
     * If the exception is caused by an input mismatch, print the line number and
     * column number of the offending token
     *
     * @param ex The exception that was thrown.
     */
    private void formatIfParseCancelled(
        final ParseCancellationException ex
    ) {
        System.out.println(ex.getMessage());
        if (ex.getCause() instanceof InputMismatchException cause) {
            final Token off = cause.getOffendingToken();
            this.stderr.println(
                new FormattedErrorMessage(
                    off.getLine(),
                    off.getCharPositionInLine(),
                    "Syntax error: %s not expected"
                        .formatted(off.getText())
                ).asString()
            );
        }
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
