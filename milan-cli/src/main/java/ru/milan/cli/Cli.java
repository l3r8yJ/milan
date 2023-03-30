package ru.milan.cli;

import picocli.CommandLine;

/**
 * @todo #20:30min/DEV Write unit tests.
 * Unit test for cli interface.
 */
/**
 * @todo #20:30min/DEV Add CLI to some package manager.
 * Add the CLI to some package manager, such as npm and etc
 */
public class Cli {
    public static void main(final String[] args) {
        System.exit(
            new CommandLine(new ProgramSource()).execute(args)
        );
    }
}
