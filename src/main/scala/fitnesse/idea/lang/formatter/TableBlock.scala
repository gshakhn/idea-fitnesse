package fitnesse.idea.lang.formatter

import java.util

import com.intellij.formatting._
import com.intellij.lang.ASTNode
import com.intellij.psi.tree.TokenSet
import fitnesse.idea.lang.lexer.FitnesseTokenType
import fitnesse.idea.lang.parser.FitnesseElementType

import scala.collection.JavaConversions._

class TableBlock(node: ASTNode) extends BasicASTBlock(node) {

  val formatter = new FormatterImpl

  // TODO: Determine number of columns, so we can assign the right alignment to the right cell (cell/row end?)

  override lazy val getSubBlocks: util.List[Block] = findSubBlocks(n => n.getElementType match {
    case FitnesseElementType.ROW | FitnesseElementType.SCRIPT_ROW => new TableBlock(n)
    case FitnesseElementType.CELL | FitnesseElementType.FIXTURE_CLASS => new TableBlock(n)
    case _ => new LeafBlock(n)
  })

  override def isLeaf: Boolean = getSubBlocks.isEmpty

  override def getSpacing(child1: Block, child2: Block): Spacing = {
    val type1 = if (child1 != null) child1.asInstanceOf[ASTBlock].getNode.getElementType else null
    val type2 = child2.asInstanceOf[ASTBlock].getNode.getElementType
    println(s"matching ${type1} and ${type2}")

    (type1, type2) match {
        /*
         * Leading spaces:
         */
      case (FitnesseTokenType.TABLE_START | FitnesseTokenType.ROW_END, FitnesseElementType.ROW) =>
        // Indent for first column
        createSpacing(1)
      case (FitnesseTokenType.CELL_END, FitnesseElementType.TABLE_TYPE | FitnesseElementType.FIXTURE_CLASS | FitnesseTokenType.WORD) =>
        // Fixture class line (with arguments)
        createSpacing(1)
      case (FitnesseTokenType.CELL_END, FitnesseElementType.CELL | FitnesseElementType.DECISION_INPUT | FitnesseElementType.DECISION_OUTPUT | FitnesseElementType.QUERY_OUTPUT) =>
        // Second and subsequent lines
        createSpacing(1)

      /*
       * Trailing spaces:
       */
      case (FitnesseElementType.ROW, FitnesseTokenType.ROW_END | FitnesseTokenType.TABLE_END) =>
        // last column
        createSpacing(7)
      case (FitnesseElementType.TABLE_TYPE | FitnesseElementType.FIXTURE_CLASS | FitnesseTokenType.WORD, FitnesseTokenType.CELL_END) =>
        // Fixture class line (with arguments)
        createSpacing(7)
      case (FitnesseElementType.CELL | FitnesseElementType.DECISION_INPUT | FitnesseElementType.DECISION_OUTPUT | FitnesseElementType.QUERY_OUTPUT,
            FitnesseTokenType.CELL_END) =>
        // Second and subsequent lines
        createSpacing(7)
      case _ => null
    }
  }

  private def createSpacing(spacing: Int) = Spacing.createSpacing(spacing, spacing, 0, true, 0)
}
