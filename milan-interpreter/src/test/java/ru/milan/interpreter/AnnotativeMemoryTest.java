package ru.milan.interpreter;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        MatcherAssert.assertThat(
            "Doesn't contains wrong atom",
            this.mem.get("notMyAtom"),
            Matchers.nullValue()
        );
    }

    @Test
    void freesMemory() {
        this.mem.assign("1-2-3", new Value(123));
        MatcherAssert.assertThat(
            "Before clear",
            this.mem.get("1-2-3"),
            Matchers.notNullValue()
        );
        this.mem.free();
        MatcherAssert.assertThat(
            "Memory free",
            this.mem.get("1-2-3"),
            Matchers.nullValue()
        );
    }
}
