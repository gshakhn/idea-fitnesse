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

%{
Character lastChar = null;
%}

LINE_TERMINATOR = \n|\r\n
WORD            = ([A-Z][a-z0-9]+)

%state TABLE_START
%state ROW_START
%state ROW
%state ROW_END

%%

<YYINITIAL> {LINE_TERMINATOR}  {return FitnesseElementType.LINE_TERMINATOR();}
<YYINITIAL> "|"                {yybegin(TABLE_START); yypushback(1); return FitnesseElementType.TABLE_START();}
<YYINITIAL> .                  {lastChar = yycharat(0); return FitnesseElementType.REGULAR_TEXT();}

<TABLE_START> "|"              {yybegin(ROW_START); yypushback(1); return FitnesseElementType.ROW_START();}

<ROW_START> "|"                {yybegin(ROW); return FitnesseElementType.CELL_DELIM();}

<ROW> "|"                      {return FitnesseElementType.CELL_DELIM();}
<ROW> [^\n\r\|]+               {return FitnesseElementType.CELL_TEXT();}
<ROW> {LINE_TERMINATOR}        {yybegin(ROW_END); return FitnesseElementType.ROW_END();}

<ROW_END> {LINE_TERMINATOR}    {yybegin(YYINITIAL); return FitnesseElementType.TABLE_END();}
<ROW_END> "|"                  {yybegin(ROW_START); yypushback(1); return FitnesseElementType.ROW_START();}


<YYINITIAL> ({WORD}) (  (({WORD})+[A-Z]?) | [A-Z]  )
                                {
                                 if (lastChar != null && Character.isJavaIdentifierPart(lastChar)) {
                                     yypushback(yylength() - 1);
                                     lastChar = yycharat(0);
                                     return FitnesseElementType.REGULAR_TEXT();
                                   } else {
                                     return FitnesseElementType.WIKI_WORD();
                                  }
                                }