/*
 * Copyright (c) 2020 Broadcom.
 * The term "Broadcom" refers to Broadcom Inc. and/or its subsidiaries.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Broadcom, Inc. - initial API and implementation
 */

lexer grammar PanelDefinitionLexer;

channels{COMMENTS, TECHNICAL}
@lexer::members {
   boolean enableCobolSpecialSeparators = true;
}
EJECT: E J E C T DOT_FS? -> channel(HIDDEN);
SKIP1 : S K I P '1' DOT_FS? -> channel(HIDDEN);
SKIP2 : S K I P '2' DOT_FS? -> channel(HIDDEN);
SKIP3 : S K I P '3' DOT_FS? -> channel(HIDDEN);

// keywords
ADD : A D D;
MODIFY : M O D I F Y;
DELETE : D E L E T E;
MAP : M A P;
MFLD : M F L D;
DFLD : D F L D;
EDIT : E D I T;
ERROR : E R R O R;
INCORRECT : I N C O R R E C T;
FIELDS : F I E L D S;
ATTRIBUTES : A T T R I B U T E S;
AT : A T;
OF : O F;
DELIMIT : D E L I M I T;
SKIP_WORD : S K I P;
INPUT : I N P U T;
PAD : P A D;
VALUE : V A L U E;
END : E N D;
AUTOPANEL : A U T O P A N E L;
DECIMAL : D E C I M A L;
POINT : P O I N T;
IS : I S;
COMMA: C O M M A;
CORRECT : C O R R E C T;
BRIGHT : B R I G H T;
FOR : F O R;
MSG : M S G;
PREFIX : P R E F I X;
DC : D C;
CURRENCY_SYMBOL : [\p{Sc}];
DEVICES : D E V I C E S;
USING : U S I N G;
RECORDS : R E C O R D S;
REC : R E C;
ROLE : R O L E;
ROLENAME : R O L E N A M E;
NOEDIT : N O E D I T;
CURSOR : C U R S O R;
RESET : R E S E T;
NORESET : N O R E S E T;
MODIFIED : M O D I F I E D;
MOD : M O D;
LOCK : L O C K;
UNLOCK : U N L O C K;
KEYBOARD : K E Y B O A R D;
KEY : K E Y;
ALARM : A L A R M;
NOALARM : N O A L A R M;
STARTPRT : S T A R T P R T;
NOPRT : N O P R T;
NLCR : N L C R;
FORTY_CR : '40CR';
SIXTYFOUR_CR : '64CR';
EIGHTY_CR : '80CR';
PAGEABLE : P A G E A B L E;
PAGE : P A G E;
NON_PAG : N O N P A G;
NON_PAGEABLE : N O N P A G E A B L E;
PERIOD_WORD : P E R I O D;
DEC : D E C;
SYS : S Y S;
SYSTEM : S Y S T E M;
HELP : H E L P;
ON : O N;
ATTR : A T T R;
ALPHANUMERIC : A L P H A N U M E R I C;
ALPHA : A L P H A;
NUMERIC : N U M E R I C;
NUM : N U M;
PROTECTED : P R O T E C T E D;
PROT : P R O T;
UNPROTECTED : U N P R O T E C T E D;
UNPROT : U N P R O T;
DETECT : D E T E C T;
DETECTABLE : D E T E C T A B L E;
NONDETECT : N O N D E T E C T;
NONDETECTABLE : N O N D E T E C T A B L E;
DISPLAY : D I S P L A Y;
DISP : D I S P;
DARK : D A R K;
MDT : M D T;
NOMDT : N O M D T;
NOBLINK : N O B L I N K;
BLINK : B L I N K;
REVERSE_VIDEO : 'REVERSE-VIDEO';
REV : R E V;
NORMAL_VIDEO : 'NORMAL-VIDEO';
NORM : N O R M;
UNDER : U N D E R;
UNDERSCORE : U N D E R S C O R E;
NOUNDER : N O U N D E R;
NOUNDERSCORE : N O U N D E R S C O R E;
ALLLINE : A L L L I N E;
ALLL : A L L L;
NOLINE : N O L I N E;
NOLI : N O L I;
LEFTLINE : L E F T L I N E;
RIGHTLINE : R I G H T L I N E;
TOPLINE : T O P L I N E;
BOTTOMLINE : B O T T O M L I N E;
LEFT : L E F T;
RIGHT : R I G H T;
TOP : T O P;
BOTTOM : B O T T O M;
NOLEFTLINE : N O L E F T L I N E;
NORIGHTLINE : N O R I G H T L I N E;
NOTOPLINE : N O T O P L I N E;
NOBOTTOMLINE : N O B O T T O M L I N E;
NOLEFT : N O L E F T;
NORIGHT : N O R I G H T;
NOTOP : N O T O P;
NOBOTTOM : N O B O T T O M;
RES : R E S;
RESIDENT : R E S I D E N T;
NONRES : N O N R E S;
NONRESIDENT : N O N R E S I D E N T;
SOUND : S O U N D;
ORIGIN : O R I G I N;
ALL : A L L;

BLUE : B L U E;
RED : R E D;
PINK : P I N K;
GREEN : G R E E N;
TURQUOISE : T U R Q U O I S E;
YELLOW : Y E L L O W;
WHITE : W H I T E;
NOCOLOR : N O C O L O R;

BL : B L;
PIN : P I N;
GRE : G R E;
TUR : T U R;
YEL : Y E L;
WHI : W H I;
NOC : N O C;



// whitespace, line breaks, comments, ...
NEWLINE : '\r'? '\n' -> channel(HIDDEN);
COMMENTLINE : COMMENTTAG ~('\n' | '\r')* -> channel(COMMENTS);
WS : [ \t\f]+ -> channel(HIDDEN);
COMPILERLINE : DOUBLEMORETHANCHAR ~('\n' | '\r')* -> channel(HIDDEN);
// period full stopPosition
DOT_FS : '.' EOF?;

LEVEL_NUMBER : ([1-9])|([0][1-9])|([1234][0-9]);
LEVEL_NUMBER_66 : '66';
LEVEL_NUMBER_77 : '77';
LEVEL_NUMBER_88 : '88';

INTEGERLITERAL : (PLUSCHAR | MINUSCHAR)? DIGIT+ | LEVEL_NUMBER;
NUMERICLITERAL : (PLUSCHAR | MINUSCHAR)? DIGIT* (DOT_FS | COMMACHAR) DIGIT+ (('e' | 'E') (PLUSCHAR | MINUSCHAR)? DIGIT+)?;
NONNUMERICLITERAL : UNTRMSTRINGLITERAL | STRINGLITERAL | DBCSLITERAL | HEXNUMBER | NULLTERMINATED;

HEX_NUMBERS : HEXNUMBER;

ASTERISKCHAR : '*';
DOUBLEASTERISKCHAR : '**';
COLONCHAR : ':';
COMMACHAR : ',';
COMMENTTAG : '*>' -> channel(COMMENTS);
DOLLARCHAR : '$';
DOUBLEMORETHANCHAR : '>>';

EQUALCHAR : '=';
LESSTHANCHAR : '<';
LESSTHANOREQUAL : '<=';
LPARENCHAR : '(';
MINUSCHAR : '-';
MORETHANCHAR : '>';
MORETHANOREQUAL : '>=';
NOTEQUALCHAR : '<>';
PLUSCHAR : '+';
RPARENCHAR : ')';
SLASHCHAR : '/';

// Special IF for dialect
UNDERSCORECHAR : '_';
DIALECT_IF: UNDERSCORECHAR I F UNDERSCORECHAR;

// Dialect filler
ZERO_WIDTH_SPACE: '\u200B';
U_CHAR: U;
IDENTIFIER : [a-zA-Z0-9][-_a-zA-Z0-9]*;
VERSION : V E R S I O N;
VER : V E R;

// treat all the non-processed tokens as errors
ERRORCHAR : . ;

fragment HEXNUMBER : X '"' [0-9A-Fa-f]+ '"' | X '\'' [0-9A-Fa-f]+ '\'';
fragment NULLTERMINATED : Z '"' (~["\n\r] | '""' | '\'')* '"' | Z '\'' (~['\n\r] | '\'\'' | '"')* '\'';
fragment STRINGLITERAL : '"' (~["\n\r] | '""' | '\'')* '"' | '\'' (~['\n\r] | '\'\'' | '"')* '\'';
fragment UNTRMSTRINGLITERAL : '"' (~["\n\r] | '""' | '\'')* | '\'' (~['\n\r] | '\'\'' | '"')*;
fragment DBCSLITERAL : [GN] '"' (~["\n\r] | '""' | '\'')* '"' | [GN] '\'' (~['\n\r] | '\'\'' | '"')* '\'';

fragment OCT_DIGIT        : [0-8] ;
fragment DIGIT: OCT_DIGIT | [9];

// case insensitive chars
fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
fragment J:('j'|'J');
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');

// COBOL special separators
SEMICOLONSEPARATOR : '; ' {enableCobolSpecialSeparators}? -> channel(HIDDEN);
COMMASEPARATOR : ', '  {enableCobolSpecialSeparators}? -> channel(HIDDEN);

// -------------------------------------------------------
// PICTURECLAUSE mode
// -------------------------------------------------------
PICTUREIS : IS;
WS2 : [ \t\f]+ -> channel(HIDDEN);
TEXT : ~('\n' | '\r');
LParIntegralRPar: LPARENCHAR INTEGERLITERAL RPARENCHAR;
