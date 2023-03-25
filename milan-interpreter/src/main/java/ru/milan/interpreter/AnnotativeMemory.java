package ru.milan.interpreter;

import java.util.HashMap;
import java.util.Map;
import org.cactoos.text.FormattedText;
import ru.milan.interpreter.exception.AtomNotDefinedException;

/**
 * The memory, annotation way.
 */
public final class AnnotativeMemory implements Memory<Atom> {

    private final Map<String, Atom> memory = new HashMap<>(0);

    @Override
    public Atom get(final String name) {
        final Atom atom = this.memory.get(name);
        if (null == atom) {
            throw new AtomNotDefinedException(
                new FormattedText("Atom {%s} is not defined!", name)
            );
        }
        return atom;
    }

    @Override
    public void assign(final String name, final Atom value) {
        this.memory.put(name, value);
    }

    @Override
    public void free() {
        this.memory.clear();
    }
}
