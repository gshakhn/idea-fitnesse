package fitnesse.idea.lang.highlighting

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import fitnesse.idea.lang.lexer.{FitnesseLexer, FitnesseTokenType}

class FitnesseHighlighter extends SyntaxHighlighterBase {

  def getHighlightingLexer = new FitnesseLexer

  override def getTokenHighlights(p1: IElementType) = {
    p1 match {
      case FitnesseTokenType.WIKI_WORD => Array(FitnesseHighlighter.WIKI_WORD)
//      case FitnesseTokenType.CELL_DELIM => Array(FitnesseHighlighter.CELL_DELIM)
      case FitnesseTokenType.TABLE_START => Array(FitnesseHighlighter.TABLE_START)
//      case FitnesseTokenType.TABLE_TYPE => Array(FitnesseHighlighter.TABLE_TYPE)
      case _ => Array.empty
    }
  }
}

object FitnesseHighlighter {
  final val WIKI_WORD = TextAttributesKey.createTextAttributesKey("FITNESSE.WIKI_WORD", DefaultLanguageHighlighterColors.KEYWORD)
//  final val CELL_DELIM = TextAttributesKey.createTextAttributesKey("FITNESSE.CELL", DefaultLanguageHighlighterColors.DOT)
  final val TABLE_START = TextAttributesKey.createTextAttributesKey("FITNESSE.TABLE_START", DefaultLanguageHighlighterColors.STRING)
//  final val TABLE_TYPE = TextAttributesKey.createTextAttributesKey("FITNESSE.TABLE_TYPE", DefaultLanguageHighlighterColors.METADATA)
}