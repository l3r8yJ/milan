package ru.milan.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import lombok.RequiredArgsConstructor;
import org.cactoos.text.FormattedText;
import ru.milan.parser.exception.BreakLoop;
import ru.milan.parser.exception.InterpretationException;
import ru.milan.parser.exception.WrongTypeException;

/**
 * @todo #5 Write unit tests for MilanVisitor.
 * Write a unit tests for MilanVisitor.
 */
/**
 * It's a visitor that visits the parse tree and executes the program.
 */
@RequiredArgsConstructor
public final class MilanVisitor extends ProgramBaseVisitor<Value> {

    private final InputStream stdin;
    private final PrintStream stdout;
    private final PrintStream stderr;
    private final Memory<Value> memory;

    private PrintStream print;
    private BufferedReader input;

    @Override
    public Value visitInt(final ProgramParser.IntContext ctx) {
        return new Value(Integer.parseInt(ctx.INT().getText()));
    }

    @Override
    public Value visitId(final ProgramParser.IdContext ctx) {
        return this.memory.get(ctx.getText());
    }

    @Override
    public Value visitAssignStmt(final ProgramParser.AssignStmtContext ctx) {
        final Value value = this.visit(ctx.expr());
        this.memory.assign(ctx.ID().getText(), value);
        return value;
    }

    @Override
    public Value visitMultiplication(final ProgramParser.MultiplicationContext ctx) {
        return this.visit(ctx.expr(0)).mul(this.visit(ctx.expr(1)));
    }

    @Override
    public Value visitDivision(final ProgramParser.DivisionContext ctx) {
        return this.visit(ctx.expr(0)).div(this.visit(ctx.expr(1)));
    }

    @Override
    public Value visitAddition(final ProgramParser.AdditionContext ctx) {
        return this.visit(ctx.expr(0)).add(this.visit(ctx.expr(1)));
    }

    @Override
    public Value visitSubtracting(final ProgramParser.SubtractingContext ctx) {
        return this.visit(ctx.expr(0)).sub(this.visit(ctx.expr(1)));
    }

    @Override
    public Value visitGreaterThan(final ProgramParser.GreaterThanContext ctx) {
        return this.visit(ctx.expr(0)).gt(this.visit(ctx.expr(1)));
    }

    @Override
    public Value visitGreaterEqualsThan(final ProgramParser.GreaterEqualsThanContext ctx) {
        return this.visit(ctx.expr(0)).gte(this.visit(ctx.expr(1)));
    }

    @Override
    public Value visitLessThan(final ProgramParser.LessThanContext ctx) {
        return this.visit(ctx.expr(0)).lt(this.visit(ctx.expr(1)));
    }

    @Override
    public Value visitLessEqualsThan(final ProgramParser.LessEqualsThanContext ctx) {
        return this.visit(ctx.expr(0)).lte(this.visit(ctx.expr(1)));
    }

    @Override
    public Value visitNotEquals(final ProgramParser.NotEqualsContext ctx) {
        return this.visit(ctx.expr(0)).neq(this.visit(ctx.expr(1)));
    }

    @Override
    public Value visitEquals(final ProgramParser.EqualsContext ctx) {
        return this.visit(ctx.expr(0)).eq(this.visit(ctx.expr(1)));
    }

    @Override
    public Value visitStmt(final ProgramParser.StmtContext ctx) {
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
    public Value visitIfStmt(final ProgramParser.IfStmtContext ctx) {
        final Value condition = this.visit(ctx.expr());
        if (condition.isTrue()) {
            return this.visit(ctx.block());
        } else {
            if (null != ctx.elseStmt()) {
                return visit(ctx.elseStmt());
            }
        }
        return condition;
    }

    @Override
    public Value visitOutputStmt(final ProgramParser.OutputStmtContext ctx) {
        final Value value = visit(ctx.expr());
        this.print.println(value.internal());
        return value;
    }

    @Override
    public Value visitReadStmt(final ProgramParser.ReadStmtContext ctx) {
        this.print.print(this.visit(ctx.ID()).internal());
        final String name = ctx.ID().getText();
        try {
            final String line = this.input.readLine();
            final Value value = new Value(Integer.parseInt(line));
            this.memory.assign(name, value);
            return value;
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Value visitWhileStmt(final ProgramParser.WhileStmtContext ctx) {
        Value condition = this.visit(ctx.expr());
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
    private void clean() {
        this.print.close();
    }
}
