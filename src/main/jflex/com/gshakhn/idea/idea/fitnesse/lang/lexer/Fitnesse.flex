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
WIKI_WORD =       ([A-Z][a-z0-9]+)+([A-Z][a-z0-9]+)

%state TABLE_HEADER
%state TABLE_DATA
%state QUERY_TABLE_HEADER
%state QUERY_COLUMN_ROW
%state IMPORT_ROW_START
%state IMPORT_CELL
%state IMPORT_ROW_END

%%

<YYINITIAL> {LINE_TERMINATOR}                    {return FitnesseElementType.LINE_TERMINATOR();}
<YYINITIAL> {WIKI_WORD}                          {return FitnesseElementType.WIKI_WORD();}
<YYINITIAL> "\|Ordered query:"                   {yybegin(QUERY_TABLE_HEADER); return FitnesseElementType.ORDERED_QUERY_TABLE();}
<YYINITIAL> "\|Subset query:"                    {yybegin(QUERY_TABLE_HEADER); return FitnesseElementType.SUBSET_QUERY_TABLE();}
<YYINITIAL> "|Query:"                            {yybegin(QUERY_TABLE_HEADER); return FitnesseElementType.QUERY_TABLE();}
<YYINITIAL> "|Table:"                            {yybegin(TABLE_HEADER); return FitnesseElementType.TABLE_TABLE();}
<YYINITIAL> \|Import[ \t]*\|{LINE_TERMINATOR}    {yybegin(IMPORT_ROW_START); return FitnesseElementType.IMPORT_TABLE();}
<YYINITIAL> "|"                                  {yybegin(TABLE_HEADER); return FitnesseElementType.DECISION_TABLE();}
<YYINITIAL> .                                    {return FitnesseElementType.REGULAR_TEXT();}

<TABLE_HEADER> "|"                               {return FitnesseElementType.CELL_DELIM();}
<TABLE_HEADER> \|{LINE_TERMINATOR}               {yybegin(TABLE_DATA); return FitnesseElementType.TABLE_HEADER_END();}
<TABLE_HEADER> [^\r\n\|]+                        {return FitnesseElementType.TABLE_HEADER_CELL();}

<QUERY_TABLE_HEADER> "|"                         {return FitnesseElementType.CELL_DELIM();}
<QUERY_TABLE_HEADER> [^\r\n\|]+                  {return FitnesseElementType.TABLE_HEADER_CELL();}
<QUERY_TABLE_HEADER> \|{LINE_TERMINATOR}         {yybegin(QUERY_COLUMN_ROW); return FitnesseElementType.TABLE_HEADER_END();}

<QUERY_COLUMN_ROW> \|{LINE_TERMINATOR}           {yybegin(TABLE_DATA); return FitnesseElementType.QUERY_COLUMN_ROW_END();}
<QUERY_COLUMN_ROW> "|"                           {return FitnesseElementType.CELL_DELIM();}
<QUERY_COLUMN_ROW> [^\r\n\|]+                    {return FitnesseElementType.QUERY_COLUMN_CELL();}

<TABLE_DATA> "|"                                 {return FitnesseElementType.CELL_DELIM();}
<TABLE_DATA> \|{LINE_TERMINATOR}                 {return FitnesseElementType.ROW_END();}
<TABLE_DATA> [^\r\n\|]+                          {return FitnesseElementType.CELL_TEXT();}
<TABLE_DATA> {LINE_TERMINATOR}                   {yybegin(YYINITIAL); return FitnesseElementType.TABLE_END();}

<IMPORT_ROW_START> "|"                           {yybegin(IMPORT_CELL); return FitnesseElementType.CELL_DELIM();}
<IMPORT_ROW_START> {LINE_TERMINATOR}             {yybegin(YYINITIAL); return FitnesseElementType.IMPORT_TABLE_END();}

<IMPORT_CELL> [^\r\n\|]+                         {yybegin(IMPORT_ROW_END); return FitnesseElementType.IMPORT_CELL();}

<IMPORT_ROW_END> \|{LINE_TERMINATOR}             {yybegin(IMPORT_ROW_START); return FitnesseElementType.IMPORT_ROW_END();}