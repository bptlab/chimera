grammar XORGrammar;

options
{
  language = Java;
  output = AST;
}

@lexer::header {
package de.uni_potsdam.hpi.bpt.bp2014.jcore;
}

@parser::header {
package de.uni_potsdam.hpi.bpt.bp2014.jcore;
}


COMPARISON: '=' | '<' | '>' | '<=' | '>=';
DOT: '.';
CHARAC: 'A'..'Z' | 'a'..'z' | '0'..'9';
OPERATOR: ' & ' | ' | ' | '&' | '|';
NAME: (CHARAC)* | (CHARAC)* DOT (CHARAC)*;
EXPR2: NAME COMPARISON NAME;
expr: EXPR2 (OPERATOR EXPR2)*;

