package com.gshakhn.idea.idea.fitnesse.lang.lexer

import com.intellij.psi.tree.IElementType
import com.gshakhn.idea.idea.fitnesse.lang.FitnesseLanguage

class FitnesseElementType(debugName: String) extends IElementType(debugName, FitnesseLanguage.INSTANCE) {
  override def toString = "Fitnesse:" + this.debugName
}

object FitnesseElementType {
  final val WIKI_WORD: IElementType = new FitnesseElementType("WIKI_WORD")
  final val REGULAR_TEXT: IElementType = new FitnesseElementType("REGULAR_TEXT")
  final val CELL_DELIM: IElementType = new FitnesseElementType("CELL_DELIM")
  final val CELL_TEXT: IElementType = new FitnesseElementType("CELL_TEXT")

  final val LINE_TERMINATOR: IElementType = new FitnesseElementType("LINE_TERMINATOR")

  final val TABLE_START: IElementType = new FitnesseElementType("TABLE_START")
  final val TABLE_END: IElementType = new FitnesseElementType("TABLE_END")
  final val ROW_START: IElementType = new FitnesseElementType("ROW_START")
  final val ROW_END: IElementType = new FitnesseElementType("ROW_END")
}
