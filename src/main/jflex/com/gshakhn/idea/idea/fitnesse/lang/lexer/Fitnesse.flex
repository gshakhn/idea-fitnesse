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
WIKI_WORD = ([A-Z][a-z0-9]+)+([A-Z][a-z0-9]+)

%%

<YYINITIAL> {LINE_TERMINATOR}  {return FitnesseElementType.LINE_TERMINATOR();}
<YYINITIAL> {WIKI_WORD}  {return FitnesseElementType.WIKI_WORD();}
<YYINITIAL> .           {return FitnesseElementType.REGULAR_TEXT();}