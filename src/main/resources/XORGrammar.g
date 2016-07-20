grammar XORGrammar;

options
{
  language = Java;
  output = AST;
}

@lexer::header {
package de.hpi.bpt.chimera.jcore;
}

@parser::header {
package de.hpi.bpt.chimera.jcore;
}

fragment STRING: '#'?('A'..'Z' | 'a'..'z' | '0'..'9')+;
COMPARISON: '!'?('=' | '<' | '>' | '<=' | '>=');
fragment DOT: '.';
OPERATOR: ' & ' | ' | ' | '&' | '|';
NAME: STRING | STRING  DOT STRING;
expr2: NAME COMPARISON NAME;
expr: expr2 (OPERATOR expr2)*;

