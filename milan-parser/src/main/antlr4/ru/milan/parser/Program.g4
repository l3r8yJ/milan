grammar Program;

prog: 'BEGIN' stmt_list 'END';

stmt_list: stmt+
         ;

stmt: assign_stmt | read_stmt | while_stmt | if_stmt | output_stmt | incr_stmt;

assign_stmt: ID ':=' expr ';';

read_stmt: ID ':=' 'READ' ';';

while_stmt: 'WHILE' expr 'DO' stmt_list 'ENDDO';

if_stmt: 'IF' expr 'THEN' stmt_list ( 'ELSE' stmt_list )? 'ENDIF';

incr_stmt: ID '++' ';' ;

output_stmt: 'OUTPUT' '(' expr ')' ';' ;

expr: ID | INT | expr op=('*'|'/'|'+'|'-'|'='|'=='|'>'|'<'|'<>'|'<='|'>=') expr;

ID: [a-zA-Z0-9]+;
INT: [0-9]+;

WS: [ \t\r\n]+ -> skip;
