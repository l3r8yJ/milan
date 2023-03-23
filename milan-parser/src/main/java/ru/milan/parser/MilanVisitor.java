package ru.milan.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public final class MilanVisitor extends ProgramBaseVisitor<Integer> {
    private final Map<String, Integer> memory = new HashMap<>(0);

    @Override
    public Integer visitAssignStmt(final ProgramParser.AssignStmtContext ctx) {
        final int value = this.visit(ctx.expr());
        this.memory.put(ctx.ID().getText(), value);
        return value;
    }

    @Override
    public Integer visitOutputStmt(final ProgramParser.OutputStmtContext ctx) {
        final Integer value = this.visit(ctx.expr());
        System.out.println(value);
        return 0;
    }

    @Override
    public Integer visitReadStmt(final ProgramParser.ReadStmtContext ctx) {
        try (final Scanner scanner = new Scanner(System.in)) {
            final Integer value = scanner.nextInt();
            this.memory.put(ctx.ID().getText(), value);
            return value;
        }
    }
}
