package ru.milan.interpreter;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link Value}.
 */
final class ValueTest {

    private Atom alpha;

    private Atom omega;

    @BeforeEach
    void setUp() {
        this.alpha = new Value(1);
        this.omega = new Value(4);
    }

    @Test
    void writesCorrectAddition() {
        MatcherAssert.assertThat(
            "1 + 4 equal to 5",
            this.alpha.add(this.omega).asInteger(),
            Matchers.equalTo(5)
        );
    }

    @Test
    void writesCorrectSubtraction() {
        MatcherAssert.assertThat(
            "4 - 1 equal to 3",
            this.omega.sub(this.alpha).asInteger(),
            Matchers.equalTo(3)
        );
    }

    @Test
    void writesCorrectMultiplication() {
        MatcherAssert.assertThat(
            "4 * 2 equal to 8",
            this.alpha.mul(new Value(2)).mul(this.omega).asInteger(),
            Matchers.equalTo(8)
        );
    }

    @Test
    void writesCorrectDivision() {
        MatcherAssert.assertThat(
            "4 / 2 equal to 2",
            new Value(4).div(new Value(2)).asInteger(),
            Matchers.equalTo(2)
        );
        MatcherAssert.assertThat(
            "5 / 2 equal to 2",
            new Value(5).div(new Value(2)).asInteger(),
            Matchers.equalTo(2)
        );
    }

    @Test
    void comparesCorrectly() {
        MatcherAssert.assertThat(
            "2 is equal to 2",
            new Value(2).eq(new Value(2)),
            Matchers.equalTo(Value.TRUE)
        );
        MatcherAssert.assertThat(
            "3 is greater than to 2",
            new Value(3).gt(new Value(2)),
            Matchers.equalTo(Value.TRUE)
        );
        MatcherAssert.assertThat(
            "2 is lesser than to 3",
            new Value(2).lt(new Value(3)),
            Matchers.equalTo(Value.TRUE)
        );
        MatcherAssert.assertThat(
            "6 is greater than or equal to 3",
            new Value(6).gte(new Value(2)),
            Matchers.equalTo(Value.TRUE)
        );
        MatcherAssert.assertThat(
            "3 is greater than or equal to 3",
            new Value(3).gte(new Value(2)),
            Matchers.equalTo(Value.TRUE)
        );
        MatcherAssert.assertThat(
            "3 is less than or equal to 3",
            new Value(3).lte(new Value(3)),
            Matchers.equalTo(Value.TRUE)
        );
        MatcherAssert.assertThat(
            "89 is less than or equal to 1253",
            new Value(89).lte(new Value(1253)),
            Matchers.equalTo(Value.TRUE)
        );
    }

    @Test
    void writesCorrectOr() {
        MatcherAssert.assertThat(
            "false || true == true",
            Value.FALSE.or(Value.TRUE),
            Matchers.is(Value.TRUE)
        );
        MatcherAssert.assertThat(
            "false || false == false",
            Value.FALSE.or(Value.FALSE),
            Matchers.is(Value.FALSE)
        );
    }

    @Test
    void writesCorrectAnd() {
        MatcherAssert.assertThat(
            "false && true == false",
            Value.FALSE.and(Value.TRUE),
            Matchers.is(Value.FALSE)
        );
        MatcherAssert.assertThat(
            "true && true == true",
            Value.TRUE.and(Value.TRUE),
            Matchers.is(Value.TRUE)
        );
    }

    @Test
    void checksOnNumber() {
        MatcherAssert.assertThat(
            "0 – is number",
            new Value(0).isNaN(),
            Matchers.is(false)
        );
    }

    @Test
    void assertsNumberRight() {
        Assertions.assertDoesNotThrow(
            () -> this.alpha.assertNumber()
        );
    }
}
