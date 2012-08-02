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
%state ROW_START
%state ROW
%state ROW_END

%%

<YYINITIAL> {LINE_TERMINATOR}               {return FitnesseElementType.LINE_TERMINATOR();}
<YYINITIAL> " "                             {return FitnesseElementType.WHITE_SPACE();}
<YYINITIAL> \t                              {return FitnesseElementType.WHITE_SPACE();}
<YYINITIAL> "|"                             {yybegin(TABLE_START); yypushback(1); return FitnesseElementType.TABLE_START();}
<YYINITIAL> [A-Z]([a-z0-9]+[A-Z][a-z0-9]*)+ {return FitnesseElementType.WIKI_WORD();}
<YYINITIAL> [:jletterdigit:]+               {return FitnesseElementType.WORD();}
<YYINITIAL> .                               {return FitnesseElementType.REGULAR_TEXT();}

<TABLE_START> "|"                           {yybegin(ROW_START); yypushback(1); return FitnesseElementType.ROW_START();}

<ROW_START> "|"                             {yybegin(ROW); return FitnesseElementType.CELL_DELIM();}

<ROW> "|"                                   {return FitnesseElementType.CELL_DELIM();}
<ROW> [^\n\r\|]+                            {return FitnesseElementType.CELL_TEXT();}
<ROW> {LINE_TERMINATOR}                     {yybegin(ROW_END); return FitnesseElementType.ROW_END();}

<ROW_END> {LINE_TERMINATOR}                 {yybegin(YYINITIAL); return FitnesseElementType.TABLE_END();}
<ROW_END> "|"                               {yybegin(ROW_START); yypushback(1); return FitnesseElementType.ROW_START();}


