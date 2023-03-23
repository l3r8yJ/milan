grammar Program;

prog: BEGIN statements END;

statements: stmt+ ;

stmt: assignStmt | readStmt | whileStmt | ifStmt | outputStmt | incrStmt;

assignStmt: ID ASSIGN expr SEMICOLON;

readStmt: ID ASSIGN READ SEMICOLON;

whileStmt: WHILE expr DO statements ENDDO;

ifStmt: IF expr THEN statements (ELSE statements)? ENDIF;

incrStmt: ID '++' SEMICOLON
        | '++' ID SEMICOLON;

outputStmt: OUTPUT LBRACKET expr RBRACKET SEMICOLON ;

expr: ID
    | INT
    | LBRACKET expr RBRACKET
    | expr op=( MATHS | BOOLS ) expr;

MATHS: SUB
    | ADD
    | DIV
    | MUL
    ;

BOOLS: LTE
    | GTE
    | LT
    | GT
    | NEQ
    | EQ
    ;

BEGIN: 'BEGIN';
END: 'END';
IF: 'IF';
THEN: 'THEN';
ELSE: 'ELSE';
ENDIF: 'ENDIF';
WHILE: 'WHILE';
DO: 'DO';
ENDDO: 'ENDDO';
READ: 'READ';
OUTPUT: 'OUTPUT';

ASSIGN: ':=';
SEMICOLON: ';';
LBRACKET: '(';
RBRACKET: ')';

LTE: '<=';
GTE: '>=';
LT: '<';
GT: '>';
NEQ: '<>';
EQ: '==';

SUB: '-';
ADD: '+';
DIV: '/';
MUL: '*';

ID: [a-zA-Z0-9]+;
INT: [0-9]+;

WS: [ \t\r\n]+ -> skip;
