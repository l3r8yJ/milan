package ru.milan.parser;

import java.util.HashMap;
import java.util.Map;

public class MilanListener extends ProgramBaseListener {
    final Map<String, Integer> memory;

    public MilanListener() {
        this.memory = new HashMap<>(0);
    }

}
