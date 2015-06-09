package fitnesse.idea.lang.highlighting

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.{SyntaxHighlighter, SyntaxHighlighterBase, SyntaxHighlighterFactory}
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.tree.IElementType
import fitnesse.idea.lang.lexer.{FitnesseLexer, FitnesseTokenType}

class FitnesseHighlighter extends SyntaxHighlighterBase {

  def getHighlightingLexer = new FitnesseLexer

  override def getTokenHighlights(elementType: IElementType) = {
    println("highlight " + elementType)
    elementType match {
      case FitnesseTokenType.WIKI_WORD => Array(FitnesseHighlighter.WIKI_WORD)
//      case FitnesseTokenType.CELL_DELIM => Array(FitnesseHighlighter.CELL_DELIM)
      case FitnesseTokenType.TABLE_START | FitnesseTokenType.ROW_START | FitnesseTokenType.CELL_START => Array(FitnesseHighlighter.TABLE_START)
//      case FitnesseTokenType.TABLE_TYPE => Array(FitnesseHighlighter.TABLE_TYPE)
      case _ => Array.empty
    }
  }
}

object FitnesseHighlighter {
  final val WIKI_WORD = TextAttributesKey.createTextAttributesKey("FITNESSE.WIKI_WORD", DefaultLanguageHighlighterColors.KEYWORD)
  final val TABLE_START = TextAttributesKey.createTextAttributesKey("FITNESSE.TABLE_START", DefaultLanguageHighlighterColors.STRING)
}


class FitnesseSyntaxHighlighterFactory extends SyntaxHighlighterFactory {

  override def  getSyntaxHighlighter(project: Project, virtualFile: VirtualFile): SyntaxHighlighter = {
    return new FitnesseHighlighter();
  }
}