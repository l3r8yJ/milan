import java.io.IOException;
import org.cactoos.io.ResourceOf;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.milan.interpreter.AnnotativeMemory;
import ru.milan.interpreter.MilanInterpreter;

class EntrySpec {

    private MilanInterpreter interpreter;

    @BeforeEach
    void setUp() {
        this.interpreter = new MilanInterpreter(
            new AnnotativeMemory(),
            new ResourceOf("program.mil"),
            System.in,
            System.out,
            System.err
        );
    }

    @Test
    void executesRealProgram() {
        assertDoesNotThrow(() -> this.interpreter.run());
    }
}