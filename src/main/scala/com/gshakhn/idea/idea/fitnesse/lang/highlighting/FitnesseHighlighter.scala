package com.gshakhn.idea.idea.fitnesse.lang.highlighting

import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.gshakhn.idea.idea.fitnesse.lang.lexer.{FitnesseElementType, FitnesseLexer}

class FitnesseHighlighter extends SyntaxHighlighterBase {
  def getHighlightingLexer = new FitnesseLexer

  def getTokenHighlights(p1: IElementType) = {
    p1 match {
      case FitnesseElementType.WIKI_WORD => Array(FitnesseHighlighterColors.WIKI_WORD)
      case FitnesseElementType.CELL_TEXT => Array(FitnesseHighlighterColors.CELL_TEXT)
      case _ => Array.empty
    }
  }
}
