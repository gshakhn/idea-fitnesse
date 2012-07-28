package com.gshakhn.idea.idea.fitnesse.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

%%

%class _FitnesseLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

LINE_TERMINATOR = \n|\r\n
WIKI_WORD =       ([A-Z][a-z0-9]+)+([A-Z][a-z0-9]+)

%state TABLE_HEADER
%state TABLE_DATA

%%

<YYINITIAL> {LINE_TERMINATOR}  {return FitnesseElementType.LINE_TERMINATOR();}
<YYINITIAL> {WIKI_WORD}        {return FitnesseElementType.WIKI_WORD();}
<YYINITIAL> \|                 {yybegin(TABLE_HEADER); return FitnesseElementType.DECISION_TABLE();}
<YYINITIAL> .                  {return FitnesseElementType.REGULAR_TEXT();}

<TABLE_HEADER> \|                 {return FitnesseElementType.CELL_DELIM();}
<TABLE_HEADER> {LINE_TERMINATOR}  {yybegin(TABLE_DATA);return FitnesseElementType.TABLE_HEADER_END();}
<TABLE_HEADER> [^\r\n\|]+         {return FitnesseElementType.CELL_TEXT();}

<TABLE_DATA>     \|                                  {return FitnesseElementType.CELL_DELIM();}
<TABLE_DATA>     {LINE_TERMINATOR}{LINE_TERMINATOR}  {yybegin(YYINITIAL);return FitnesseElementType.TABLE_END();}
<TABLE_DATA>     {LINE_TERMINATOR}                   {return FitnesseElementType.ROW_END();}
<TABLE_DATA>     [^\r\n\|]+                          {return FitnesseElementType.CELL_TEXT();}