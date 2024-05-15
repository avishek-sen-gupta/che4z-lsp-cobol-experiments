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
   : programUnit
   ;

programUnit
   : statement*;

commaSeparator: COMMACHAR | COMMASEPARATOR;

// --- identification division --------------------------------------------------------------------

identificationDivision
   : statement*
   ;

statement : verb MAP IDENTIFIER versionClause? dateTimeClause? messagePrefix?
            autoPanelClause systemClause? residencyClause? usingClause?
            editClause? cursorClause? resetClause? lockKeyboardClause? alarmClause?
            prtClause? carriageReturnClause? pageableClause?
            decimalPointClause? helpClause? onEditErrorClause? originClause?
            ;

dateTimeClause :DATETIME (IS | EQUALCHAR)? DATE_TIME_STAMP;
messagePrefix: MSG PREFIX (IS | EQUALCHAR)? (DC | IDENTIFIER);
autoPanelClause : AUTOPANEL;
systemClause : (SYS | SYSTEM) (IS | EQUALCHAR) IDENTIFIER;
residencyClause : (RES | RESIDENT) | (NONRES | NONRESIDENT);
usingClause : USING recordKeyword? LPARENCHAR records RPARENCHAR;
editClause : (EDIT | NOEDIT);
cursorClause : CURSOR AT IDENTIFIER;
resetClause : (RESET | NORESET) (MODIFIED | MOD);
lockKeyboardClause : (LOCK | UNLOCK) (KEYBOARD | KEY)?;
alarmClause : (ALARM | NOALARM);
prtClause : (STARTPRT | NOPRT);
carriageReturnClause : (NLCR | FORTY_CR | SIXTYFOUR_CR | EIGHTY_CR);
pageableClause : (pageable | nonPageable);
decimalPointClause : decimalPoint (IS | EQUALCHAR)? (COMMA | PERIOD_WORD);
helpClause : HELP;
onEditErrorClause : ON EDIT ERROR incorrectFieldsAttributesClause? correctFieldsAttributesClause? soundClause?;
incorrectFieldsAttributesClause : INCORRECT FIELDS (ATTR | ATTRIBUTES) EQUALCHAR? attributesList;
correctFieldsAttributesClause : CORRECT FIELDS (ATTR | ATTRIBUTES) EQUALCHAR? attributesList;
soundClause : SOUND ALARM | NOALARM;
originClause : ORIGIN FOR (ALL | records) (IS | EQUALCHAR) LPARENCHAR row column RPARENCHAR;

//deviceCodesList : LPARENCHAR deviceCodes RPARENCHAR;
//deviceCodes : commaDelimitedDeviceCode* deviceCode;
//commaDelimitedDeviceCode : deviceCode commaSeparator;
//deviceCode : IDENTIFIER;

row : INTEGERLITERAL;
column : INTEGERLITERAL;

attributesList : LPARENCHAR (attributeAlphanumericOption | attributeProtectedOption | skipOption | attributeDetectableOption |
                    atributeDisplayOption | attributeMdtOption | attributeBlinkOption | attributeVideoOption |
                    attributeUnderscoreOption | attributeAllLineOption | attributeLeftLineOption | attributeRightLinePption |
                    attributeBottomLineOption | attributeTopLineOption | attributeColorOption) RPARENCHAR;

attributeAlphanumericOption : (ALPHANUMERIC | ALPHA) | (NUMERIC | NUM);
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
attributeColorOption : blueOption | redOption | pinkOption | greenOption | turquoiseOption | yellowOption |
                       whiteOption | noColorOption;

blueOption : BL | BLUE;
redOption : RED;
pinkOption : PIN | PINK;
greenOption : GRE | GREEN;
turquoiseOption : TUR | TURQUOISE;
yellowOption : YEL | YELLOW;
whiteOption : WHI | WHITE;
noColorOption : NOC | NOCOLOR;

decimalPoint : (DEC | DECIMAL) POINT;
pageable : (PAGE | PAGEABLE);
nonPageable : (NON_PAG | NON_PAGEABLE);

recordKeyword : RECORDS | REC;
records : recordIdentifier recordIdentifier*;
//commaDelimitedRecordIdentifier : COMMACHAR recordIdentifier;
recordIdentifier : IDENTIFIER version? roleClause?;
roleClause : (ROLE | ROLENAME) IDENTIFIER;
verb : (ADD | MODIFY DELETE);
version : (VERSION | VER);
versionClause : version (IS | EQUALCHAR)? IDENTIFIER;
// statement clauses ----------------------------------

// identifier ----------------------------------

generalIdentifier
   : qualifiedDataName
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

// qualified data name ----------------------------------

qualifiedDataName
   : variableUsageName tableCall? referenceModifier?
   ;

tableCall
   : LPARENCHAR (ALL | arithmeticExpression) (COMMACHAR? (ALL | arithmeticExpression))* RPARENCHAR
   ;

// names ----------------------------------

alphabetName
   : cobolWord
   ;

cdName
   : cobolWord
   ;

className
   : cobolWord
   ;

dataName
   : cobolWord
   ;

variableUsageName
   : cobolWord
   ;

fileName
   : cobolWord
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
