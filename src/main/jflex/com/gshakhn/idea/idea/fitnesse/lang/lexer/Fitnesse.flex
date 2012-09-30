package com.gshakhn.idea.idea.fitnesse.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

%%

%class _FitnesseLexer
%implements FlexLexer
%unicode
%ignorecase
%function advance
%type IElementType
%eof{  return;
%eof}

LINE_TERMINATOR = \n|\r\n

%state TABLE_START
%state FIRST_ROW_START
%state ROW_START
%state FIRST_ROW_CELL
%state FIRST_ROW_CELL_COLON
%state ROW
%state ROW_END_1
%state ROW_END_2

%%

<YYINITIAL> {LINE_TERMINATOR}                 {return FitnesseTokenType.LINE_TERMINATOR();}
<YYINITIAL> " "                               {return FitnesseTokenType.WHITE_SPACE();}
<YYINITIAL> \t                                {return FitnesseTokenType.WHITE_SPACE();}
<YYINITIAL> "|"                               {yybegin(TABLE_START); yypushback(1); return FitnesseTokenType.TABLE_START();}
<YYINITIAL> [A-Z]([a-z0-9]+[A-Z][a-z0-9]*)+   {return FitnesseTokenType.WIKI_WORD();}
<YYINITIAL> [:jletterdigit:]+                 {return FitnesseTokenType.WORD();}
<YYINITIAL> \.                                {return FitnesseTokenType.PERIOD();}
<YYINITIAL> \>                                {return FitnesseTokenType.GT();}
<YYINITIAL> \<                                {return FitnesseTokenType.LT();}
<YYINITIAL> .                                 {return FitnesseTokenType.REGULAR_TEXT();}

<TABLE_START> "|"                             {yybegin(FIRST_ROW_START); yypushback(1); return FitnesseTokenType.ROW_START();}

<FIRST_ROW_START> "|"                         {yybegin(FIRST_ROW_CELL); return FitnesseTokenType.CELL_DELIM();}
<ROW_START> "|"                               {yybegin(ROW); return FitnesseTokenType.CELL_DELIM();}

<FIRST_ROW_CELL> script[ \t]*                 {yybegin(ROW); return FitnesseTokenType.TABLE_TYPE();}
<FIRST_ROW_CELL> comment[ \t]*                {yybegin(ROW); return FitnesseTokenType.TABLE_TYPE();}
<FIRST_ROW_CELL> scenario[ \t]*               {yybegin(ROW); return FitnesseTokenType.TABLE_TYPE();}
<FIRST_ROW_CELL> library[ \t]*                {yybegin(ROW); return FitnesseTokenType.TABLE_TYPE();}
<FIRST_ROW_CELL> import[ \t]*                 {yybegin(ROW); return FitnesseTokenType.TABLE_TYPE();}
<FIRST_ROW_CELL> [^\n\r\|\:]+:                {yybegin(FIRST_ROW_CELL_COLON); yypushback(1); return FitnesseTokenType.TABLE_TYPE();}
<FIRST_ROW_CELL> [^\n\r\|\:]+                 {yybegin(ROW); return FitnesseTokenType.CELL_TEXT();}

<FIRST_ROW_CELL_COLON> :                      {yybegin(ROW); return FitnesseTokenType.COLON();}

<ROW> "|"                                     {return FitnesseTokenType.CELL_DELIM();}
<ROW> [^\n\r\|]+                              {return FitnesseTokenType.CELL_TEXT();}
<ROW> {LINE_TERMINATOR}                       {yybegin(ROW_END_1); return FitnesseTokenType.LINE_TERMINATOR();}
<ROW> <<EOF>>                                 {yybegin(ROW_END_2); return FitnesseTokenType.ROW_END();}

<ROW_END_1> {LINE_TERMINATOR}                 {yybegin(ROW_END_2); yypushback(yylength()); return FitnesseTokenType.ROW_END();}
<ROW_END_1> .                                 {yybegin(ROW_END_2); yypushback(1); return FitnesseTokenType.ROW_END();}

<ROW_END_2> "|"                               {yybegin(ROW_START); yypushback(1); return FitnesseTokenType.ROW_START();}
<ROW_END_2> {LINE_TERMINATOR}                 {yybegin(YYINITIAL); yypushback(yylength()); return FitnesseTokenType.TABLE_END();}
<ROW_END_2> .                                 {yybegin(YYINITIAL); yypushback(1); return FitnesseTokenType.TABLE_END();}
<ROW_END_2> <<EOF>>                           {yybegin(YYINITIAL); return FitnesseTokenType.TABLE_END();}