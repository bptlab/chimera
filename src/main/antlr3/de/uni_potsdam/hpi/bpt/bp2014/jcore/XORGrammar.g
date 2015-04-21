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

fragment CHARAC: '$'?('A'..'Z' | 'a'..'z' | '0'..'9')+;
COMPARISON: '=' | '<' | '>' | '<=' | '>=';
fragment DOT: '.';
OPERATOR: ' & ' | ' | ' | '&' | '|';
NAME: CHARAC | CHARAC  DOT CHARAC;
expr2: NAME COMPARISON NAME;
expr: expr2 (OPERATOR expr2)*;

