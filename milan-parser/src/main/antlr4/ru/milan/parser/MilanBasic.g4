// @todo #5 Implement the grammar.
// We have to implement basic grammar for g4.

// @todo #5 Implement the Parser.
// We have to implement Parser.

// @todo #5 Implement the Error handler.
// We have to implement Error handler.

// @todo #5 Implement the Runtime.
// We have to implement Runtime.
grammar MilanBasic;

    BEGIN : 'BEGIN' ;

    DO : 'DO' ;

    ELSE : 'ELSE' ;

    END : 'END' ;

    ENDDO : 'ENDDO' ;

    ENDIF : 'ENDIF' ;

    IF : 'IF' ;

    OUTPUT : 'OUTPUT' ;

    READ : 'READ' ;

    THEN : 'THEN' ;

    WHILE : 'WHILE' ;

    SWITCH : 'SWITCH' ;

    CASE : 'CASE' ;

    DEFAULT : 'DEFAULT' ;

    NOT_EQUAL_SIGN : LESSER_SIGN GREATER_SIGN ;

    GREATER_EQUALS : GREATER_SIGN EQUALITY_SIGN ;

    LESSER_EQUALS : LESSER_SIGN EQUALITY_SIGN ;

    INCREMENT : PLUS PLUS ;

    ASSIGN : ':=' ;

    MULTIPLIER_SIGN : '*' ;

    DIVIDER_SIGN : '/' ;

    EQUALITY_SIGN : '=' ;

    GREATER_SIGN : '>' ;

    LESSER_SIGN : '<' ;

    PLUS : '+' ;

    MINUS : '-' ;

    L_CURL_BRACKET : '{' ;

    R_CURL_BRACKET : '}' ;

    L_BRACKET : '(' ;

    R_BRACKET : ')' ;

    COLON : ':' ;

    SEMICOLON : ';' ;

    COMMENTS_START : '//' ;

    LITERAL : [A-Za-z_-] [a-zA-Z0-9_-]*;

    INT : [0-9]+ ;

    NEWLINE : '\r'? '\n' ;

    WS : [ \t]+ -> skip ;
