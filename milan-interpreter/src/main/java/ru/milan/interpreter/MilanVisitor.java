package ru.milan.interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import org.cactoos.text.FormattedText;
import ru.milan.interpreter.exception.InterpretationException;
import ru.milan.interpreter.exception.WrongTypeException;
import ru.milan.interpreter.message.FormattedErrorMessage;

/**
 * It's a visitor that visits the parse tree and executes the program.
 */
public final class MilanVisitor extends ProgramBaseVisitor<Atom> {

    /**
     * The input.
     */
    private final InputStream stdin;

    /**
     * The output.
     */
    private final PrintStream stdout;

    /**
     * Error output.
     */
    private final PrintStream stderr;

    /**
     * Runtime memory.
     */
    private final Memory<Atom> memory;

    /**
     * Wrapper wor printing.
     */
    private PrintStream print;

    /**
     * Wrapper wor input.
     */
    private BufferedReader input;

    /**
     * Ctor.
     */
    public MilanVisitor() {
        this(System.in, System.out, System.err, new AnnotativeMemory());
    }

    /**
     * Ctor.
     */
    public MilanVisitor(final Memory<Atom> memory) {
        this(System.in, System.out, System.err, memory);
    }

    /**
     * Ctor.
     *
     * @param stdin Standard input
     */
    public MilanVisitor(final InputStream stdin) {
        this(stdin, System.out, System.err, new AnnotativeMemory());
    }

    /**
     * @param out Standard output
     * @param memory Runtime memory
     */
    public MilanVisitor(final PrintStream out, final Memory<Atom> memory) {
        this(System.in, out, System.err, memory);
    }

    /**
     * Ctor.
     *
     * @param stdin Standard input
     * @param stdout Standard output
     */
    public MilanVisitor(final InputStream stdin, final PrintStream stdout) {
        this(stdin, stdout, System.err, new AnnotativeMemory());
    }

    /**
     * Ctor.
     *
     * @param stdin Standard input
     * @param stdout Standard output
     * @param stderr Standard errors
     */
    public MilanVisitor(
        final InputStream stdin,
        final PrintStream stdout,
        final PrintStream stderr
    ) {
        this(stdin, stdout, stderr, new AnnotativeMemory());
    }

    /**
     * Ctor.
     *
     * @param stdin Standard input
     * @param stdout Standard output
     * @param stderr Standard errors
     * @param memory Interpretation memory
     */
    public MilanVisitor(
        final InputStream stdin,
        final PrintStream stdout,
        final PrintStream stderr,
        final Memory<Atom> memory
    ) {
        this.stdin = stdin;
        this.stdout = stdout;
        this.stderr = stderr;
        this.memory = memory;
        this.init();
    }

    @Override
    public Atom visitProg(final ProgramParser.ProgContext ctx) {
        this.init();
        try {
            ctx.children.forEach(tkn -> {
                if (tkn instanceof ProgramParser.AssignStmtContext ass) {
                    this.visitAssignStmt(ass);
                }
            });
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
        final Atom value = this.visit(ctx.expressions());
        this.memory.assign(ctx.ID().getText(), value);
        return value;
    }

    @Override
    public Atom visitIncrStmt(final ProgramParser.IncrStmtContext ctx) {
        final String name = ctx.ID().getText();
        final Atom increment = this.memory.get(name).add(new Value(1));
        this.memory.assign(name, increment);
        return increment;
    }

    @Override
    public Atom visitBrackets(final ProgramParser.BracketsContext ctx) {
        final Atom result;
        if (ctx.expr() instanceof ProgramParser.AdditionContext addition) {
            result = this.visitAddition(addition);
        } else if (ctx.expr() instanceof ProgramParser.MultiplicationContext mult) {
            result = this.visitMultiplication(mult);
        } else if (ctx.expr() instanceof ProgramParser.SubtractingContext sub) {
            result = this.visitSubtracting(sub);
        } else if (ctx.expr() instanceof ProgramParser.DivisionContext div) {
            result = this.visitDivision(div);
        } else {
            result = super.visitBrackets(ctx);
        }
        return result;
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
        } catch (final RuntimeException ex) {
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
        final Atom condition = this.visit(ctx.expressions());
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
        final Atom value = this.visit(ctx.expressions());
        this.print.println(value.asInteger());
        return value;
    }

    @Override
    public Atom visitReadStmt(final ProgramParser.ReadStmtContext ctx) {
        final String name = ctx.ID().getText();
        try {
            final String line = this.input.readLine();
            final Atom value = new Value(Integer.valueOf(line));
            this.memory.assign(name, value);
            return value;
        } catch (final NumberFormatException ex) {
            throw new WrongTypeException(
                new FormattedErrorMessage(
                    ctx.getStart().getLine(),
                    ctx.getStart().getCharPositionInLine(),
                    String.format("Atom {%s} isn't integer", name)
                ).asString(),
                ex
            );
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Atom visitWhileStmt(final ProgramParser.WhileStmtContext ctx) {
        Atom condition = this.visit(ctx.expressions());
        while (condition.isTrue()) {
            this.visit(ctx.block());
            condition = this.visit(ctx.expressions());
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
