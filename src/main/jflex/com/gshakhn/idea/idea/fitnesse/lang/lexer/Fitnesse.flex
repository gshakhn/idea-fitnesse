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

WIKIWORD = ([A-Z][a-z]+)+([A-Z][a-z]+)

%%

<YYINITIAL> {

  {WIKIWORD} {return FitnesseElementType.WIKI_WORD();}

}
