grammar Program;

prog: BEGIN block END;

block: stmt*;

stmt: assignStmt | readStmt | whileStmt | ifStmt | outputStmt | incrStmt;

assignStmt: ID ASSIGN expr SEMICOLON;

readStmt: ID ASSIGN READ LBRACKET expr RBRACKET SEMICOLON;

whileStmt: WHILE expr DO block ENDDO;

ifStmt: IF expr THEN block (elseStmt)? ENDIF;

elseStmt: ELSE block;

incrStmt: ID INCR SEMICOLON # PreIncrement
        | INCR ID SEMICOLON # PostIncrement
        ;

outputStmt: OUTPUT LBRACKET expr RBRACKET SEMICOLON ;

expr: ID # Id
    | INT # Int
    | expr MUL expr          # Multiplication
    | expr DIV expr          # Division
    | expr ADD expr          # Addition
    | expr SUB expr          # Subtracting
    | expr EQ  expr          # Equals
    | expr NEQ expr          # NotEquals
    | expr GT  expr          # GreaterThan
    | expr GTE expr          # GreaterEqualsThan
    | expr LT  expr          # LessThan
    | expr LTE expr          # LessEqualsThan
    | LBRACKET expr RBRACKET # Brackets
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
INCR: '++';
COMMENT: '//' .*? '\r'? '\n' -> skip;

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
BOOL: 'true'
    | 'false'
    ;

WS: [ \t\r\n]+ -> skip;