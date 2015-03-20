package com.gshakhn.idea.idea.fitnesse.lang.highlighting

import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.gshakhn.idea.idea.fitnesse.lang.lexer.{FitnesseTokenType, FitnesseLexer}

class FitnesseHighlighter extends SyntaxHighlighterBase {
  def getHighlightingLexer = new FitnesseLexer

  override def getTokenHighlights(p1: IElementType) = {
    p1 match {
      case FitnesseTokenType.WIKI_WORD => Array(FitnesseHighlighterColors.WIKI_WORD)
      case FitnesseTokenType.CELL_DELIM => Array(FitnesseHighlighterColors.CELL_DELIM)
      case FitnesseTokenType.CELL_TEXT => Array(FitnesseHighlighterColors.CELL_TEXT)
      case FitnesseTokenType.TABLE_TYPE => Array(FitnesseHighlighterColors.TABLE_TYPE)
      case _ => Array.empty
    }
  }
}
