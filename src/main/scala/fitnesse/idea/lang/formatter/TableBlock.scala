package fitnesse.idea.lang.formatter

import com.intellij.formatting._
import com.intellij.lang.ASTNode
import fitnesse.idea.lang.lexer.FitnesseTokenType
import fitnesse.idea.lang.parser.FitnesseElementType
import fitnesse.idea.rt.TableFormatter

import scala.collection.JavaConverters._


case class CellBlock(node: ASTNode, row: Int, col: Int) extends LeafBlock(node) {
  override def toString: String = s"${getClass.getSimpleName}:${node.getElementType}(${row},${col})"
}

case class BarBlock(node: ASTNode) extends LeafBlock(node)

class TableBlock(node: ASTNode) extends BasicASTBlock(node) {

  def createCounter: () => Int = {var i = -1; () => { i += 1; i } }

  lazy val width: Integer = node.getText.trim.length

  // TODO: deal with empty cells
  // TODO: deal with table prefix (!|, -!|)
  override lazy val subBlocks: List[ASTBlock] = {
    val nextRowId = createCounter
    findSubBlocks(n => n.getElementType match {
      case FitnesseTokenType.TABLE_START | FitnesseTokenType.TABLE_END | FitnesseTokenType.ROW_END =>
        List(BarBlock(n))
      case FitnesseElementType.ROW | FitnesseElementType.SCRIPT_ROW =>
        val rowId = nextRowId()
        findInRow(n, rowId, createCounter)
      case _ =>
        List(new LeafBlock(n))
    })
  }

  def findInRow(n: ASTNode, rowId: Int, nextCellId: () => Int): List[ASTBlock] = {
    findSubBlocks(n, n => n.getElementType match {
      case FitnesseTokenType.CELL_END =>
        List(BarBlock(n))
      case FitnesseElementType.CELL | FitnesseElementType.FIXTURE_CLASS | FitnesseElementType.TABLE_TYPE | FitnesseElementType.DECISION_INPUT | FitnesseElementType.DECISION_OUTPUT | FitnesseElementType.QUERY_OUTPUT | FitnesseElementType.IMPORT =>
        List(CellBlock(n, rowId, nextCellId()))
      case FitnesseElementType.SCENARIO_NAME =>
        findInRow(n, rowId, nextCellId)
      case _ =>
        List(new LeafBlock(n))
    })
  }

  /** Returns shortest possible list of lists xss such that
    *   - xss.flatten == xs
    *   - No sublist in xss contains an element matching p in its tail
    */
  def groupPrefix[T](xs: List[T])(p: T => Boolean): List[List[T]] = xs match {
    case List() => List()
    case x :: xs1 =>
      val (ys, zs) = xs1 span (!p(_))
      (x :: ys) :: groupPrefix(zs)(p)
  }

  def calculateWidths(blocks: List[ASTBlock]): List[Int] = blocks match {
    case (barBlock @ BarBlock(bar)) :: (cellBlock: CellBlock) :: rest if bar.getElementType == FitnesseTokenType.TABLE_START =>
      (barBlock.width - 1 + cellBlock.width) :: calculateWidths(rest)
    case (cellBlock : CellBlock) :: rest =>
      cellBlock.width :: calculateWidths(rest)
    case BarBlock(bar1) :: BarBlock(bar2) :: rest =>
      // do something with empty cells:
      0 :: calculateWidths(rest)
    case _ :: rest => calculateWidths(rest)
    case Nil => Nil
  }

  lazy val cellBlocks: List[List[Int]] = splitBlocksByRow.map(row => calculateWidths(row))

  def splitBlocksByRow: List[List[ASTBlock]] = {
    groupPrefix(subBlocks)(_.getNode.getElementType == FitnesseTokenType.ROW_END)
  }

  lazy val tableFormatter: TableFormatter = new TableFormatter(cellBlocks.map(row => row.map(Integer.valueOf).asJava).asJava)

  def rightPadding(row: Int): Int = tableFormatter.rightPadding(row)

  def rightPadding(row: Int, col: Int): Int = tableFormatter.rightPadding(row, col)

  override def isLeaf: Boolean = subBlocks.isEmpty

  override def getSpacing(child1: Block, child2: Block): Spacing = {
    (child1, child2) match {
      case (BarBlock(bar), CellBlock(cell, row, col)) =>
        createSpacing(1)
      case (CellBlock(cell, row, col), BarBlock(bar)) =>
        createSpacing(tableFormatter.rightPadding(row, col))
      case (BarBlock(bar1), BarBlock(bar2)) =>
        // create some spacing here...
        null
      case _ => null
    }
  }

  private def createSpacing(spacing: Int) = Spacing.createSpacing(spacing, spacing, 0, true, 0)


}


/**
 * TableFormatterBlock is slightly different in that it refers up to the table for padding and all.
 */
trait TableFormatterBlock {

  val tableFormatter: TableFormatter

  val coordinates: Tuple2[Int, Int]

  def rightPadding: Int = tableFormatter.rightPadding(coordinates._1, coordinates._2)
}
