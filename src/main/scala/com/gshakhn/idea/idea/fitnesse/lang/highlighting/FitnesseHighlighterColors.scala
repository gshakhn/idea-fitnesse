package com.gshakhn.idea.idea.fitnesse.lang.highlighting

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

object FitnesseHighlighterColors {
  final val WIKI_WORD = TextAttributesKey.createTextAttributesKey("FITNESSE.WIKI_WORD", DefaultLanguageHighlighterColors.KEYWORD)
  final val CELL_DELIM = TextAttributesKey.createTextAttributesKey("FITNESSE.CELL_DELIM", DefaultLanguageHighlighterColors.DOT)
  final val CELL_TEXT = TextAttributesKey.createTextAttributesKey("FITNESSE.CELL_TEXT", DefaultLanguageHighlighterColors.FUNCTION_CALL)
  final val TABLE_TYPE = TextAttributesKey.createTextAttributesKey("FITNESSE.TABLE_TYPE", DefaultLanguageHighlighterColors.IDENTIFIER)
}
