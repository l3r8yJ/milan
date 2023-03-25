package ru.milan.interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import lombok.RequiredArgsConstructor;
import org.cactoos.text.FormattedText;
import ru.milan.interpreter.exception.BreakLoop;
import ru.milan.interpreter.exception.InterpretationException;
import ru.milan.interpreter.exception.WrongTypeException;

/**
 * @todo #5 Write unit tests for MilanVisitor.
 * Write a unit tests for MilanVisitor.
 */
/**
 * It's a visitor that visits the parse tree and executes the program.
 */
@RequiredArgsConstructor
public final class MilanVisitor extends ProgramBaseVisitor<Atom> {

    private final InputStream stdin;
    private final PrintStream stdout;
    private final PrintStream stderr;
    private final Memory<Atom> memory;

    private PrintStream print;
    private BufferedReader input;

    @Override
    public Atom visitProg(final ProgramParser.ProgContext ctx) {
        this.init();
        try {
            return super.visitProg(ctx);
        } finally {
            this.shutdown();
        }
    }

    @Override
    public Atom visitInt(final ProgramParser.IntContext ctx) {
        return new Value(Integer.parseInt(ctx.INT().getText()));
    }

    @Override
    public Atom visitId(final ProgramParser.IdContext ctx) {
        return this.memory.get(ctx.getText());
    }

    @Override
    public Atom visitAssignStmt(final ProgramParser.AssignStmtContext ctx) {
        final Atom value = this.visit(ctx.expr());
        this.memory.assign(ctx.ID().getText(), value);
        return value;
    }

    @Override
    public Atom visitMultiplication(final ProgramParser.MultiplicationContext ctx) {
        return this.visit(ctx.expr(0)).mul(this.visit(ctx.expr(1)));
    }

    @Override
    public Atom visitDivision(final ProgramParser.DivisionContext ctx) {
        return this.visit(ctx.expr(0)).div(this.visit(ctx.expr(1)));
    }

    @Override
    public Atom visitAddition(final ProgramParser.AdditionContext ctx) {
        return this.visit(ctx.expr(0)).add(this.visit(ctx.expr(1)));
    }

    @Override
    public Atom visitSubtracting(final ProgramParser.SubtractingContext ctx) {
        return this.visit(ctx.expr(0)).sub(this.visit(ctx.expr(1)));
    }

    @Override
    public Atom visitGreaterThan(final ProgramParser.GreaterThanContext ctx) {
        return this.visit(ctx.expr(0)).gt(this.visit(ctx.expr(1)));
    }

    @Override
    public Atom visitGreaterEqualsThan(final ProgramParser.GreaterEqualsThanContext ctx) {
        return this.visit(ctx.expr(0)).gte(this.visit(ctx.expr(1)));
    }

    @Override
    public Atom visitLessThan(final ProgramParser.LessThanContext ctx) {
        return this.visit(ctx.expr(0)).lt(this.visit(ctx.expr(1)));
    }

    @Override
    public Atom visitLessEqualsThan(final ProgramParser.LessEqualsThanContext ctx) {
        return this.visit(ctx.expr(0)).lte(this.visit(ctx.expr(1)));
    }

    @Override
    public Atom visitNotEquals(final ProgramParser.NotEqualsContext ctx) {
        return this.visit(ctx.expr(0)).neq(this.visit(ctx.expr(1)));
    }

    @Override
    public Atom visitEquals(final ProgramParser.EqualsContext ctx) {
        return this.visit(ctx.expr(0)).eq(this.visit(ctx.expr(1)));
    }

    @Override
    public Atom visitStmt(final ProgramParser.StmtContext ctx) {
        try {
            return super.visitStmt(ctx);
        } catch (final WrongTypeException ex) {
            throw new InterpretationException(
                ctx.getStart().getLine(),
                ctx.getStart().getCharPositionInLine(),
                new FormattedText(
                    "Error while interpretation: '%s'\n",
                    ex.getMessage()
                ).toString(),
                ex
            );
        }
    }

    @Override
    public Atom visitIfStmt(final ProgramParser.IfStmtContext ctx) {
        final Atom condition = this.visit(ctx.expr());
        if (condition.isTrue()) {
            return this.visit(ctx.block());
        }
        if (null != ctx.elseStmt()) {
            return this.visit(ctx.elseStmt());
        }
        return condition;
    }

    @Override
    public Atom visitOutputStmt(final ProgramParser.OutputStmtContext ctx) {
        final Atom value = this.visit(ctx.expr());
        this.print.println(value.asInteger());
        return value;
    }

    @Override
    public Atom visitReadStmt(final ProgramParser.ReadStmtContext ctx) {
        this.print.print(this.visit(ctx.ID()).asInteger());
        final String name = ctx.ID().getText();
        try {
            final String line = this.input.readLine();
            final Atom value = new Value(Integer.parseInt(line));
            this.memory.assign(name, value);
            return value;
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Atom visitWhileStmt(final ProgramParser.WhileStmtContext ctx) {
        Atom condition = this.visit(ctx.expr());
        while (condition.isTrue()) {
            try {
                this.visit(ctx.block());
            } catch (final BreakLoop ignored) {
                break;
            } finally {
                condition = this.visit(ctx.expr());
            }
        }
        return new Value(0);
    }

    /**
     * Initialize the print and input variables to be used for the rest of the
     * program.
     */
    private void init() {
        this.print = new PrintStream(this.stdout);
        this.input = new BufferedReader(new InputStreamReader(this.stdin));
    }

    /**
     * This function closes the print stream
     */
    private void shutdown() {
        this.print.close();
    }
}
