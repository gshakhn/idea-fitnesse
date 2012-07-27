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

%state TABLE

%%

<YYINITIAL> {LINE_TERMINATOR}  {return FitnesseElementType.LINE_TERMINATOR();}
<YYINITIAL> {WIKI_WORD}        {return FitnesseElementType.WIKI_WORD();}
<YYINITIAL> \|                 {yybegin(TABLE);return FitnesseElementType.CELL_DELIM();}
<YYINITIAL> .                  {return FitnesseElementType.REGULAR_TEXT();}

<TABLE>     \|                                  {return FitnesseElementType.CELL_DELIM();}
<TABLE>     {LINE_TERMINATOR}{LINE_TERMINATOR}  {yybegin(YYINITIAL);return FitnesseElementType.TABLE_END();}
<TABLE>     {LINE_TERMINATOR}                   {yybegin(YYINITIAL);return FitnesseElementType.ROW_END();}
<TABLE>     [^\r\n\|]+                          {yybegin(YYINITIAL);return FitnesseElementType.CELL_TEXT();}