package com.gshakhn.idea.idea.fitnesse.lang.lexer

import com.intellij.psi.tree.IElementType
import com.intellij.psi.TokenType

class FitnesseElementType(debugName: String) extends IElementType(debugName, com.intellij.openapi.fileTypes.StdFileTypes.PROPERTIES.getLanguage) {
  override def toString = "Fitnesse:" + this.debugName
}

object FitnesseElementType {
  final val WIKI_WORD: IElementType = new FitnesseElementType("WIKI_WORD")
  final val REGULAR_TEXT: IElementType = new FitnesseElementType("REGULAR_TEXT")
  final val LINE_TERMINATOR: IElementType = new FitnesseElementType("LINE_TERMINATOR")
  final val CELL_DELIM: IElementType = new FitnesseElementType("CELL_DELIM")
  final val TABLE_HEADER_END: IElementType = new FitnesseElementType("TABLE_HEADER_END")
  final val ROW_END: IElementType = new FitnesseElementType("ROW_END")
  final val TABLE_END: IElementType = new FitnesseElementType("TABLE_END")
  final val CELL_TEXT: IElementType = new FitnesseElementType("CELL_TEXT")
  final val DECISION_TABLE: IElementType = new FitnesseElementType("DECISION_TABLE")
}
