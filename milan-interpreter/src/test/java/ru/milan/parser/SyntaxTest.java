package ru.milan.parser;

import java.nio.file.Path;
import org.cactoos.io.OutputTo;
import org.cactoos.io.ResourceOf;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SyntaxTest {

    @Test
    void parsesCorrectly(@TempDir final Path temp) {
        final Path code = temp.resolve("Code.java");
        final Syntax test = new Syntax(
            "code",
            new ResourceOf("code.ml"),
            new OutputTo(code)
        );
        assertDoesNotThrow(test::parse);
    }
}