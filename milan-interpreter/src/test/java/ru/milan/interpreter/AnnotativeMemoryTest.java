package ru.milan.interpreter;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.milan.interpreter.exception.AtomNotDefinedException;

final class AnnotativeMemoryTest {

    private Memory<Atom> mem;

    @BeforeEach
    void setUp() {
        this.mem = new AnnotativeMemory();
    }

    @Test
    void assignsAndGetsValuesCorrectly() {
        this.mem.assign("myAtom", new Value(0));
        MatcherAssert.assertThat(
            "Contains placed atom",
            this.mem.get("myAtom").asInteger(),
            Matchers.is(0)
        );
        Assertions.assertThrows(
            AtomNotDefinedException.class,
            () -> this.mem.get("notMyAtom")
        );
    }

    @Test
    void freesMemory() {
        this.mem.assign("1-2-3", new Value(123));
        MatcherAssert.assertThat(
            "Before clear",
            this.mem.get("1-2-3").asInteger(),
            Matchers.equalTo(123)
        );
        this.mem.free();
        Assertions.assertThrows(
            AtomNotDefinedException.class,
            () -> this.mem.get("1-2-3")
        );
    }
}
