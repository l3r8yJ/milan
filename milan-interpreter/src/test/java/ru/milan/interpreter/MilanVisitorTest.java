package ru.milan.interpreter;

import org.antlr.v4.runtime.RuleContext;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link MilanVisitor}.
 */
final class MilanVisitorTest {

    private ProgramVisitor<Atom> visitor;

    @BeforeEach
    void setUp() {
        final Memory<Atom> mem = new AnnotativeMemory();
        mem.assign("alpha", new Value(32));
        mem.assign("beta", new Value(2));
        this.visitor = new MilanVisitor(
            System.in,
            System.out,
            System.err,
            mem
        );
    }

    /**
     * @todo #18:60m/DEV Find a proper way to test it.
     * Turn on Disabled test and find way to test it
     */
    @Test
    @Disabled
    void visitsAddition() {
        final ProgramParser.AdditionContext  addition =
            Mockito.mock(ProgramParser.AdditionContext.class);
        addition.copyFrom(this.mockVisitorResult(ProgramParser.ExprContext.class, new Value(1)));
        addition.copyFrom(this.mockVisitorResult(ProgramParser.ExprContext.class, new Value(2)));
        MatcherAssert.assertThat(addition.expr(), Matchers.hasSize(2));
        final Integer actual = this.visitor.visit(addition).asInteger();
        MatcherAssert.assertThat(
            "Right composition",
            actual,
            Matchers.equalTo(2)
        );
    }

    private <T extends RuleContext> T mockVisitorResult(final Class<T> type, final Atom result) {
        final T mock = Mockito.mock(type);
        Mockito.when(mock.accept(this.visitor)).thenReturn(result);
        return mock;
    }

}