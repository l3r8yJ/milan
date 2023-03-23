package ru.milan.parser;

import java.util.List;
import org.cactoos.Input;
import org.cactoos.Output;
import org.cactoos.Text;
import org.cactoos.list.ListOf;
import org.cactoos.text.Split;
import org.cactoos.text.TextOf;

public final class Syntax {

    private final String name;

    private final Input input;

    private final Output target;

    public Syntax(final String name, final Input input, final Output target) {
        this.name = name;
        this.input = input;
        this.target = target;
    }

    public void parse() {
        final List<Text> lines = this.lines();
    }

    private List<Text> lines() {
        return new ListOf<>(new Split(new TextOf(this.input), "\r?\n"));
    }
}
