package ru.milan.parser;

import java.util.HashMap;
import java.util.Map;

public class MilanListener extends ProgramBaseListener {
    final Map<String, Integer> memory;

    public MilanListener() {
        this.memory = new HashMap<>(0);
    }

    @Override
    public void enterAddition(final ProgramParser.AdditionContext ctx) {
        final Integer first = Integer.parseInt(ctx.expr(0).getText());
        final Integer second = Integer.parseInt(ctx.expr(1).getText());

    }
}
