package fitnesse.idea.lang.lexer

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import fitnesse.idea.lang.FitnesseLanguage

class FitnesseTokenType(debugName: String) extends IElementType(debugName, FitnesseLanguage.INSTANCE) {
  override def toString = "FitnesseTokenType." + debugName
}

object FitnesseTokenType {
  final val WIKI_WORD: IElementType = new FitnesseTokenType("WIKI_WORD")
  final val WORD: IElementType = new FitnesseTokenType("WORD")
  final val COLON: IElementType = new FitnesseTokenType("COLON")
  final val WHITE_SPACE: IElementType = TokenType.WHITE_SPACE

  final val LINE_TERMINATOR: IElementType = new FitnesseTokenType("LINE_TERMINATOR")

  final val TABLE_START: IElementType = new FitnesseTokenType("TABLE_START")
  final val TABLE_END: IElementType = new FitnesseTokenType("TABLE_END")
  final val ROW_START: IElementType = new FitnesseTokenType("ROW_START")
  final val ROW_END: IElementType = new FitnesseTokenType("ROW_END")
  final val CELL_START: IElementType = new FitnesseTokenType("CELL_START")
  final val CELL_END: IElementType = new FitnesseTokenType("CELL_END")

  final val COLLAPSIBLE_START: IElementType = new FitnesseTokenType("COLLAPSIBLE_START")
  final val COLLAPSIBLE_END: IElementType = new FitnesseTokenType("COLLAPSIBLE_END")

  final val BOLD: IElementType = new FitnesseTokenType("BOLD")
  final val ITALIC: IElementType = new FitnesseTokenType("ITALIC")


}
