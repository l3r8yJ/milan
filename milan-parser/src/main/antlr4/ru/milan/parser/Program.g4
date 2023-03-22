grammar Program;

program: 'BEGIN' statement+ 'END';

statement: assignment | output | loop | conditional | switch_statement ;

assignment: ID ':=' EXPR ';' NEWLINE?;

output: 'OUTPUT' EXPR ';' NEWLINE?;

loop: 'WHILE' '(' EXPR ')' 'DO' statement+ 'ENDDO' ';' NEWLINE?
     | 'REPEAT' statement+ 'UNTIL' '(' EXPR ')' ';' NEWLINE?;

conditional: 'IF' '(' EXPR ')' 'THEN' statement+ ('ELSE' statement+)? 'ENDIF' NEWLINE?;

switch_statement: 'SWITCH' WS? '(' ID ')' '{' switch_case+ '}' NEWLINE?;

switch_case: 'CASE' EXPR ':' statement+ NEWLINE?;

EXPR: SIMPLE_EXPR WS? (('<'|'>'|'='|'<='|'>='|'<>'|'!=') SIMPLE_EXPR)*;

SIMPLE_EXPR: TERM (('+'|'-') TERM)*;

TERM: FACTOR (('*'|'/'|'%') FACTOR)*
    | FACTOR '++' ';' ;

FACTOR: ID
      | INT
      | '(' EXPR ')';

ID: [a-zA-Z_][a-zA-Z0-9_]*;

INT: [0-9]+;

NEWLINE: [\r?\n]+;

WS: [ \t]+ -> skip;

COMMENT: '/*' .*? '*/' -> skip;
