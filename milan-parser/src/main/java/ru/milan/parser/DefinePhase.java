package ru.milan.parser;

import com.sun.source.tree.Scope;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class DefinePhase extends ProgramBaseListener {
    final ParseTreeProperty<Scope> scopes = new ParseTreeProperty<>();
}
