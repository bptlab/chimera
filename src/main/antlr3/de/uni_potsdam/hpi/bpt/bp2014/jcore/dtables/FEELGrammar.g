grammar FEELGrammar;

options
{
  language = Java;
  output = AST;
}

@lexer::header {
package de.uni_potsdam.hpi.bpt.bp2014.jcore.dtables;
}

@parser::header {
package de.uni_potsdam.hpi.bpt.bp2014.jcore.dtables;
}

Digit : ('0'..'9')? ;
digits : Digit (Digit)* ; 
Character : ('A'..'Z')? | ('a'..'z')?;
string_literal : '"'   ( Character – ('"' | ' ') )*  '"' ;
Boolean_literal : 'true' | 'false' ;
numeric_literal : digits   ( '.'  digits )? | '.'   digits ;
addition : expression   '+'   expression ;
subtraction : expression   '-'   expression ;
multiplication : expression   '*'   expression ;
division : expression   '/'   expression ;
exponentiation : expression  '**'  expression ;
arithmetic_negation : '-'   expression ;
name : name_start   ( name_part | additional_name_symbols )* ;
name_start : Name_start_char  ( name_part_char )* ;
name_part : name_part_char   ( name_part_char )* ;
Name_start_char : '?' | ('A'..'Z')? | '_' | ('a'..'z')?  ;
name_part_char : Name_start_char | Digit ;
additional_name_symbols : '.' | '/' | '-' | '’' | '+' | '*' ; 
simple_literal : numeric_literal | string_literal | Boolean_literal ; 
arithmetic_expression: addition | subtraction | multiplication | division | exponentiation | arithmetic_negation;
simple_expression: arithmetic_expression | simple_value;
simple_expressions: simple_expression  (',' simple_expression)*; 
simple_positive_unary_test: ('<'|'<='|'>'|'>=')?  endpoint |interval;
interval : (open_interval_start | closed_interval_start)  endpoint   '..' endpoint (open_interval_end | closed_interval_end);
open_interval_start:'('|']';
closed_interval_start:'[';  
open_interval_end:')'|'[';
closed_interval_end:']';
simple_positive_unary_tests:simple_positive_unary_test (',' simple_positive_unary_test)*;
simple_unary_tests: simple_positive_unary_tests | 'not' '(' simple_unary_tests ')'| '"-';
endpoint: simple_value;
simple_value : qualified_name | simple_literal ;
qualified_name : name   ( '.'   name )* ;
expression : textual_expression ;
textual_expression : arithmetic_expression | name | '('   textual_expression   ')' ;  






