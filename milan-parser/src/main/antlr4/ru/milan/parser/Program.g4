grammar Program;

program: 'BEGIN' STATEMENT+ 'END';

STATEMENT: ASSIGMENT | OUTPUT | LOOP;

ASSIGMENT: IDENTIFIER ':=' EXPR ';';

OUTPUT: 'OUTPUT' EXPR ';';

LOOP: 'WHILE' EXPR 'DO' STATEMENT+ 'ENDDO'
     | 'REPEAT' STATEMENT+ 'UNTIL' EXPR;

EXPR: TERM (('+'|'-') TERM)*;

TERM: FACTOR (('*'|'/') FACTOR)*;

FACTOR: IDENTIFIER
      | INT
      | '(' EXPR ')';

IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;

INT : [0-9]+;

WS: [ \t\r\n]+ -> skip;

COMMENT: '/*' .*? '*/' -> skip;
