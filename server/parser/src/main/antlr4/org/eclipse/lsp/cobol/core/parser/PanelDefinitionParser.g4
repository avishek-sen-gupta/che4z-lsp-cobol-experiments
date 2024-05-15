/*
* Copyright (C) 2017, Ulrich Wolffgang <ulrich.wolffgang@proleap.io>
* All rights reserved.
*
* This software may be modified and distributed under the terms
* of the MIT license. See the LICENSE file for details.
*/

parser grammar PanelDefinitionParser;
options {tokenVocab = PanelDefinitionLexer; superClass = MessageServiceParser;}

startRule : compilationUnit EOF;

compilationUnit
   : programUnit+
   ;

programUnit
   : identificationDivision
   ;

endProgramStatement
   : END PROGRAM programName DOT_FS
   ;

commaSeparator: COMMACHAR | COMMASEPARATOR;

// --- identification division --------------------------------------------------------------------

identificationDivision
   : statement*
   ;

statement : verb MAP IDENTIFIER versionClause? dateTimeClause? messagePrefix?
            autoPanelClause systemClause? residencyClause usingClause?
            editClause cursorClause? resetClause? lockKeyboardClause? alarmClause
            prtClause carriageReturnClause pageableClause
            decimalPointClause? helpClause? onEditErrorClause? originClause? DO
            ;

dateTimeClause :DATETIME (IS | EQUALCHAR)? DATE_TIME_STAMP;
messagePrefix: MSG PREFIX (IS | EQUALCHAR)? (DC | IDENTIFIER);
autoPanelClause : AUTOPANEL;
systemClause : (SYS | SYSTEM) (IS | EQUALCHAR) IDENTIFIER;
residencyClause : (RES | RESIDENT) | (NONRES | NONRESIDENT);
usingClause : USING recordKeyword LPARENCHAR records RPARENCHAR;
editClause : (EDIT | NOEDIT);
cursorClause : CURSOR AT IDENTIFIER;
resetClause : (RESET | NORESET) (MODIFIED | MOD);
lockKeyboardClause : (LOCK | UNLOCK) (KEYBOARD | KEY)?;
alarmClause : (ALARM | NOALARM);
prtClause : (STARTPRT | NOPRT);
carriageReturnClause : (NLCR | FORTY_CR | SIXTYFOUR_CR | EIGHTY_CR);
pageableClause : (pageable | nonPageable);
decimalPointClause : decimalPoint (IS | EQUALCHAR)? (COMMA_WORD | PERIOD_WORD);
helpClause : HELP;
onEditErrorClause : ON EDIT ERROR incorrectFieldsAttributesClause? correctFieldsAttributesClause? soundClause?;
incorrectFieldsAttributesClause : INCORRECT FIELDS (ATTR | ATTRIBUTES) EQUALCHAR attributesList;
correctFieldsAttributesClause : CORRECT FIELDS (ATTR | ATTRIBUTES) EQUALCHAR attributesList;
soundClause : SOUND ALARM | NOALARM;
originaClause : ORIGIN FOR (ALL | deviceCodesList) (IS | EQUALCHAR) LPARENCHAR row column RIGHTPARENCHAR;

deviceCodesList : LPARENCHAR deviceCodes RPARENCHAR;
deviceCodes : commaDelimitedDeviceCode* deviceCode;
commaDelimitedDeviceCode : deviceCode COMMA;
deviceCode : IDENTIFIER;

row : INTEGERLITERAL;
column : INTEGERLITERAL;

attributesList : (attributeAlphanumericOption | attributeProtectedOption | skipOption | attributeDetectableOption |
                    atributeDisplayOption | attributeMdtOption | attributeBlinkOption | attributeVideoOption |
                    attributeUnderscoreOption | attributeAllLineOption | attributeLeftLineOption | attributeRightLinePption |
                    attributeBottomLineOption | attributeTopLineOption | attributeColorOption);

attributeAlphanumericOption : (ALPHANUMBERIC | ALPHA) | (NUMERIC | NUM);
attributeProtectedOption : (PROT | PROTECTED) |(UNPROT | UNPROTECTED);
skipOption : SKIP_WORD;
attributeDetectableOption : (DETECT | DETECTABLE) | (NONDETECT | NONDETECTABLE);
atributeDisplayOption : (DISP | DISPLAY) | DARK | BRIGHT;
attributeMdtOption : MDT | NOMDT;
attributeBlinkOption : BLINK | NOBLINK;
attributeVideoOption : (REV | REVERSE_VIDEO) | (NORM | NORMAL_VIDEO);
attributeUnderscoreOption : (UNDER | UNDERSCORE) | (NOUNDER | NOUNDERSCORE);
attributeAllLineOption : (ALLL | ALLLINE) | (NOLI | NOLINE);
attributeLeftLineOption : (LEFT | LEFTLINE) | (NOLEFT | NOLEFTLINE);
attributeRightLinePption : (RIGHT | RIGHTLINE) | (NORIGHT | NORIGHTLINE);
attributeBottomLineOption : (BOTTOM | BOTTOMLINE) | (NOBOTTOM | NOBOTTOMLINE);
attributeTopLineOption : (TOP | TOPLINE) | (NOTOP | NOTOPLINE);
attributeColorOption : BLUE_OPTION | RED_OPTION | PINK_OPTION | GREEN_OPTION | TURQUOISE_OPTION | YELLOW_OPTION |
                       WHITE_OPTION | NOCOLOR_OPTION;

BLUE_OPTION : BL | BLUE;
RED_OPTION : RED;
PINK_OPTION : PIN | PINK;
GREEN_OPTION : GRE | GREEN;
TURQUOISE_OPTION : TUR | TURQUOISE;
YELLOW_OPTION : YEL | YELLOW;
WHITE_OPTION : WHI | WHITE;
NOCOLOR_OPTION : NOC | NOCOLOR;

decimalPoint : (DEC | DECIMAL);
pageable : (PAGE | PAGEABLE);
nonPageable : (NON_PAG | NON_PAGEABLE);




recordKeyword : (RECORDS | REC);
records : commaDelimitedRecordIdentifier* recordIdentifier;
commaDelimitedRecordIdentifier : recordIdentifier COMMA;
recordIdentifier : IDENTIFIER version roleClause;
roleClause : (ROLE | ROLENAME) IDENTIFIER;
verb : (ADD | MODIFY DELETE);
version : (VERSION | VER);
versionClause : version (IS | EQUALCHAR)? IDENTIFIER;
// statement clauses ----------------------------------

// condition ----------------------------------

condition
   : NOT? (simpleCondition | nestedCondition | dialectNodeFiller+)
    ((AND | OR) NOT? (simpleCondition | nestedCondition | relationCombinedComparison | dialectNodeFiller+))*
   ;

simpleCondition
   : arithmeticExpression (relationCombinedComparison | fixedComparison)?
   ;

nestedCondition
   : LPARENCHAR condition RPARENCHAR
   ;

relationCombinedComparison
   : relationalOperator (arithmeticExpression
   | LPARENCHAR arithmeticExpression ((AND | OR) arithmeticExpression)+ RPARENCHAR)
   ;

fixedComparison
   : IS? NOT? (NUMERIC | ALPHABETIC | ALPHABETIC_LOWER | ALPHABETIC_UPPER | DBCS | KANJI | POSITIVE | NEGATIVE | ZERO
   | className)
   ;

relationalOperator
   : (IS | ARE)? (NOT? (GREATER THAN? | MORETHANCHAR | LESS THAN? | LESSTHANCHAR | EQUAL TO? | EQUALCHAR)
   | NOTEQUALCHAR | GREATER THAN? OR EQUAL TO? | MORETHANOREQUAL | LESS THAN? OR EQUAL TO? | LESSTHANOREQUAL)
   ;

// identifier ----------------------------------

generalIdentifier
   : specialRegister | qualifiedDataName | functionCall
   ;

functionCall
   : FUNCTION functionName (LPARENCHAR argument (COMMACHAR? argument)* RPARENCHAR)* referenceModifier?
   ;

referenceModifier
   : LPARENCHAR characterPosition COLONCHAR length? RPARENCHAR
   ;

characterPosition
   : arithmeticExpression
   ;

length
   : arithmeticExpression
   ;

argument
   : arithmeticExpression
   | TRAILING | LEADING
   ;

// qualified data name ----------------------------------

qualifiedDataName
   : variableUsageName tableCall? referenceModifier? inData*
   ;

tableCall
   : LPARENCHAR (ALL | arithmeticExpression) (COMMACHAR? (ALL | arithmeticExpression))* RPARENCHAR
   ;

specialRegister
   : ADDRESS OF generalIdentifier
   | LENGTH OF? generalIdentifier | LINAGE_COUNTER
   ;

// in ----------------------------------

inData
   : (IN | OF) variableUsageName tableCall? referenceModifier?
   ;

inSection
   : (IN | OF) sectionName
   ;

// names ----------------------------------

alphabetName
   : cobolWord
   ;

assignmentName
   : systemName
   ;

cdName
   : cobolWord
   ;

className
   : cobolWord
   ;

computerName
   : systemName
   ;

dataName
   : cobolWord
   ;

variableUsageName
   : cobolWord
   ;

environmentName
   : systemName
   ;

fileName
   : cobolWord
   ;

functionName
   : INTEGER | LENGTH | RANDOM | SUM | MAX | WHEN_COMPILED | cobolWord
   ;

indexName
   : cobolWord
   ;

libraryName
   : cobolWord
   ;

mnemonicName
   : cobolWord
   ;

paragraphName
   : cobolWord | integerLiteral
   ;

paragraphDefinitionName
   : cobolWord | integerLiteral
   ;

procedureName
   : paragraphName inSection?
   ;

programName
   : literal | cobolWord | OR | AND
   ;

recordName
   : qualifiedDataName
   ;

reportName
   : qualifiedDataName
   ;

sectionName
   : cobolWord | integerLiteral
   ;

systemName
   : cobolWord
   ;

symbolicCharacter
   : cobolWord
   ;

figurativeConstant
   : ALL literal | HIGH_VALUE | HIGH_VALUES | LOW_VALUE | LOW_VALUES | NULL | NULLS | QUOTE | QUOTES | SPACE | SPACES | ZEROS | ZEROES
   ;

booleanLiteral
   : TRUE | FALSE
   ;

numericLiteral
   : NUMERICLITERAL | ZERO | integerLiteral
   ;

integerLiteral
   : INTEGERLITERAL | LEVEL_NUMBER | LEVEL_NUMBER_66 | LEVEL_NUMBER_77 | LEVEL_NUMBER_88
   ;

//cics_conditions: EOC | EODS | INVMPSZ | INVPARTN | INVREQ | MAPFAIL | PARTNFAIL | RDATT | UNEXPIN;

literal
   : NONNUMERICLITERAL | figurativeConstant | numericLiteral | booleanLiteral | charString | dialectLiteral | utfLiteral | hexadecimalUtfLiteral
   ;

dialectLiteral: dialectNodeFiller+;

utfLiteral: U_CHAR NONNUMERICLITERAL;

hexadecimalUtfLiteral: U_CHAR HEX_NUMBERS;

charString
   : FINALCHARSTRING
   ;

// map expression
mapStatement
   : PLUSCHAR IDENTIFIER
   ;

// arithmetic expression ----------------------------------

arithmeticExpression
   : multDivs plusMinus*
   ;

plusMinus
   : (PLUSCHAR | MINUSCHAR) multDivs
   ;

multDivs
   : powers multDiv*
   ;

multDiv
   : (ASTERISKCHAR | SLASHCHAR) powers
   ;

powers
   : (PLUSCHAR | MINUSCHAR)? basis power*
   ;

power
   : DOUBLEASTERISKCHAR basis
   ;

basis
   : dialectNodeFiller | generalIdentifier | literal | LPARENCHAR arithmeticExpression RPARENCHAR
   ;

cobolWord
   : IDENTIFIER | SYMBOL | INTEGER | CHANNEL | PROCESS | REMOVE | WAIT | ANY | LIST | NAME | THREAD | U_CHAR
   | cobolKeywords
   ;

cobolKeywords
   : ADDRESS | BOTTOM | COUNT | CR | FIELD | FIRST | MMDDYYYY | PRINTER | DAY | TIME | DATE | DAY_OF_WEEK
   | REMARKS | RESUME | TIMER | TODAYS_DATE | TODAYS_NAME | TOP | YEAR | YYYYDDD | YYYYMMDD | WHEN_COMPILED
   | DISK | KEYBOARD | PORT | READER | REMOTE | VIRTUAL | LIBRARY | DEFINITION | PARSE | BOOL | ESCAPE | INITIALIZED
   | LOC | BYTITLE | BYFUNCTION | ABORT | ORDERLY | ASSOCIATED_DATA | ASSOCIATED_DATA_LENGTH | CLOSE | CURRENCY
   | DATA | DBCS | EXIT | EXTEND | INITIAL | NATIONAL | OBJECT | OFF | QUOTE | SEPARATE | SEQUENCE
   | SERVICE | STANDARD | SUPPRESS | TERMINAL | TEST | VOLATILE
   ;

dialectNodeFiller
    : ZERO_WIDTH_SPACE+
    ;
