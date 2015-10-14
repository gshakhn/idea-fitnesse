package fitnesse.idea.lang.formatter

import java.util

import com.intellij.formatting._
import com.intellij.lang.ASTNode
import fitnesse.idea.lang.lexer.FitnesseTokenType
import fitnesse.idea.lang.parser.FitnesseElementType
import fitnesse.idea.rt.TableFormatter

import scala.collection.JavaConverters._

class TableBlock(node: ASTNode) extends BasicASTBlock(node) {

  protected val nextRowOrCellId = { var i = -1; () => { i += 1; i} }

  lazy val width: Integer = node.getText.trim.length

  lazy val subBlocks = findSubBlocks(n => n.getElementType match {
    case FitnesseElementType.ROW | FitnesseElementType.SCRIPT_ROW =>
      createTableBlock(n)
    case FitnesseElementType.CELL | FitnesseElementType.FIXTURE_CLASS | FitnesseElementType.TABLE_TYPE | FitnesseElementType.DECISION_INPUT | FitnesseElementType.DECISION_OUTPUT | FitnesseElementType.QUERY_OUTPUT | FitnesseElementType.IMPORT =>
      createTableBlock(n)
    case _ => new LeafBlock(n)
  })

  def createTableBlock(node: ASTNode): TableBlock = new TableSubBlock(node, this, List(nextRowOrCellId()))

  lazy val tableBlocks: List[TableBlock] = subBlocks.filter(b => b.isInstanceOf[TableBlock]).asInstanceOf[List[TableBlock]]

  lazy val tableFormatter: TableFormatter = new TableFormatter(tableBlocks.map(row => row.tableBlocks.map(cell => cell.width).asJava).asJava)

  def rightPadding(row: Int): Int = tableFormatter.rightPadding(row)

  def rightPadding(row: Int, col: Int): Int = tableFormatter.rightPadding(row, col)

  override def isLeaf: Boolean = subBlocks.isEmpty

  override lazy val getSubBlocks: util.List[Block] = subBlocks.asJava

  override def getSpacing(child1: Block, child2: Block): Spacing = {
    val type1 = if (child1 != null) child1.asInstanceOf[ASTBlock].getNode.getElementType else null
    val type2 = child2.asInstanceOf[ASTBlock].getNode.getElementType

    (type1, type2) match {
        /*
         * Leading spaces:
         */
      case (FitnesseTokenType.TABLE_START | FitnesseTokenType.ROW_END, FitnesseElementType.ROW) =>
        // Indent for first column
        createSpacing(TableFormatter.MIN_PADDING)
      case (FitnesseTokenType.CELL_END, FitnesseElementType.TABLE_TYPE | FitnesseElementType.FIXTURE_CLASS) =>
        // Fixture class line (with arguments)
        createSpacing(TableFormatter.MIN_PADDING)
      case (FitnesseTokenType.CELL_END, FitnesseElementType.CELL | FitnesseElementType.DECISION_INPUT | FitnesseElementType.DECISION_OUTPUT | FitnesseElementType.QUERY_OUTPUT | FitnesseElementType.IMPORT) =>
        // Second and subsequent lines
        createSpacing(TableFormatter.MIN_PADDING)

      /*
       * Trailing spaces:
       */
      case (FitnesseElementType.ROW, FitnesseTokenType.ROW_END | FitnesseTokenType.TABLE_END) =>
        // last column
        val subBlock: TableSubBlock = child1.asInstanceOf[TableSubBlock]
        subBlock.coordinates match {
          case row :: Nil =>
            createSpacing(subBlock.rightPadding(row))
          case _ => createSpacing(1)
        }
      case (FitnesseElementType.TABLE_TYPE | FitnesseElementType.FIXTURE_CLASS, FitnesseTokenType.CELL_END) =>
        // Fixture class line (with arguments)
        val subBlock: TableSubBlock = child1.asInstanceOf[TableSubBlock]
        subBlock.coordinates match {
          case col :: row :: Nil =>
            createSpacing(subBlock.rightPadding(row, col))
          case _ => createSpacing(1)
        }
      case (FitnesseElementType.CELL | FitnesseElementType.DECISION_INPUT | FitnesseElementType.DECISION_OUTPUT | FitnesseElementType.QUERY_OUTPUT | FitnesseElementType.IMPORT,
            FitnesseTokenType.CELL_END) =>
        // Second and subsequent lines
        val subBlock: TableSubBlock = child1.asInstanceOf[TableSubBlock]
        subBlock.coordinates match {
          case col :: row :: Nil =>
            createSpacing(subBlock.rightPadding(row, col))
          case _ => createSpacing(1)
        }

      case _ => null
    }
  }

  private def createSpacing(spacing: Int) = Spacing.createSpacing(spacing, spacing, 0, true, 0)


}

/**
 * TableSubBlock is slightly different in that it refers up to the table for padding and all.
 *
 * @param node
 * @param table
 * @param coordinates
 */
class TableSubBlock(node: ASTNode, table: TableBlock, val coordinates: List[Int]) extends TableBlock(node) {

  override def createTableBlock(node: ASTNode): TableBlock = new TableSubBlock(node, table, nextRowOrCellId() :: coordinates)

  override def rightPadding(row: Int): Int = table.rightPadding(row)

  override def rightPadding(row: Int, col: Int): Int = table.rightPadding(row, col)
}