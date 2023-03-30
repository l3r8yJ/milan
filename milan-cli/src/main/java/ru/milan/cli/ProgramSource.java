package ru.milan.cli;

import java.io.File;
import java.util.concurrent.Callable;
import org.cactoos.io.InputOf;
import org.cactoos.text.FormattedText;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import ru.milan.interpreter.AnnotativeMemory;
import ru.milan.interpreter.MilanInterpreter;

@Command(
    name = "source",
    mixinStandardHelpOptions = true,
    version = "1.0.0",
    description = "The source of Milan program, file with extension .mil"
)
public class ProgramSource implements Callable<Integer> {

    @Parameters(
        index = "0",
        description = "Path to source file"
    )
    File source;

    @Override
    public Integer call() throws Exception {
        try {
            final MilanInterpreter interpreter = new MilanInterpreter(
                new AnnotativeMemory(),
                new InputOf(this.source),
                System.in,
                System.out,
                System.err
            );
            interpreter.run();
        } catch (final RuntimeException ex) {
            System.out.println(
                new FormattedText(
                    "Error executing the program: %s",
                    ex.getMessage()
                )
            );
            System.exit(-1);
        }
        return 0;
    }
}
