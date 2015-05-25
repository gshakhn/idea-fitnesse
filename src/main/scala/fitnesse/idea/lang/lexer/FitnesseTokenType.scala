package fitnesse.idea.lang.lexer

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import fitnesse.idea.lang.FitnesseLanguage

class FitnesseTokenType(debugName: String) extends IElementType(debugName, FitnesseLanguage.INSTANCE) {
  override def toString = "Fitnesse:" + this.debugName
}

object FitnesseTokenType {
  final val WIKI_WORD: IElementType = new FitnesseTokenType("WIKI_WORD")
  final val WORD: IElementType = new FitnesseTokenType("WORD")
  final val REGULAR_TEXT: IElementType = new FitnesseTokenType("REGULAR_TEXT")
//  final val PERIOD: IElementType = new FitnesseTokenType("PERIOD")
//  final val GT: IElementType = new FitnesseTokenType("GT")
//  final val LT: IElementType = new FitnesseTokenType("LT")
  final val COLON: IElementType = new FitnesseTokenType("COLON")
  final val WHITE_SPACE: IElementType = TokenType.WHITE_SPACE

  final val CELL_DELIM: IElementType = new FitnesseTokenType("CELL_DELIM")
  final val CELL_TEXT: IElementType = new FitnesseTokenType("CELL_TEXT")

  final val LINE_TERMINATOR: IElementType = new FitnesseTokenType("LINE_TERMINATOR")

  final val TABLE_TYPE: IElementType = new FitnesseTokenType("TABLE_TYPE")
  final val TABLE_START: IElementType = new FitnesseTokenType("TABLE_START")
  final val TABLE_END: IElementType = new FitnesseTokenType("TABLE_END")
  final val ROW_START: IElementType = new FitnesseTokenType("ROW_START")
  final val ROW_END: IElementType = new FitnesseTokenType("ROW_END")
  final val CELL_START: IElementType = new FitnesseTokenType("CELL_START")
  final val CELL_END: IElementType = new FitnesseTokenType("CELL_END")
}
