package fitnesse.idea.formatter

import com.intellij.formatting._
import com.intellij.lang.ASTNode
import fitnesse.idea.lexer.FitnesseTokenType
import fitnesse.idea.parser.FitnesseElementType
import fitnesse.idea.rt.TableFormatter

import scala.collection.JavaConverters._


case class CellBlock(node: ASTNode, row: Int, col: Int) extends LeafBlock(node) {
  override def toString: String = s"${getClass.getSimpleName}:${node.getElementType}(${row},${col})"
}

case class EmptyCellBarBlock(node: ASTNode, row: Int, col: Int) extends LeafBlock(node) {
  override def toString: String = s"${getClass.getSimpleName}:${node.getElementType}(${row},${col})"
}

case class BarBlock(node: ASTNode) extends LeafBlock(node)

class TableBlock(node: ASTNode) extends BasicASTBlock(node) {

  def createCounter: () => Int = {var i = -1; () => { i += 1; i } }

  lazy val width: Integer = node.getText.trim.length

  override lazy val subBlocks: List[ASTBlock] = {
    val nextRowId = createCounter
    var rowId = 0
    var nextCellId: () => Int = createCounter
    findSubBlocks((node, previous) => node.getElementType match {
      case FitnesseTokenType.TABLE_START =>
        List(BarBlock(node))
      case FitnesseTokenType.TABLE_END | FitnesseTokenType.ROW_END =>
        List(previous match {
          case Some(_: BarBlock) => EmptyCellBarBlock(node, rowId, nextCellId())
          case Some(_: EmptyCellBarBlock) => EmptyCellBarBlock(node, rowId, nextCellId())
          case _ => BarBlock(node)
        })
      case FitnesseElementType.ROW | FitnesseElementType.SCRIPT_ROW =>
        // Update counters:
        rowId = nextRowId()
        nextCellId = createCounter
        findInRow(node, rowId, nextCellId)
      case _ =>
        List(new LeafBlock(node))
    })
  }

  def findInRow(rowNode: ASTNode, rowId: Int, nextCellId: () => Int): List[ASTBlock] = {
    findSubBlocks(rowNode, (node, previous) => node.getElementType match {
      case FitnesseTokenType.CELL_END | FitnesseTokenType.ROW_END =>
        List(previous match {
          case Some(_: BarBlock) => EmptyCellBarBlock(node, rowId, nextCellId())
          case Some(_: EmptyCellBarBlock) => EmptyCellBarBlock(node, rowId, nextCellId())
          case None => /* first in line */ EmptyCellBarBlock(node, rowId, nextCellId())
          case _ => BarBlock(node)
        })
      case FitnesseElementType.CELL | FitnesseElementType.FIXTURE_CLASS | FitnesseElementType.TABLE_TYPE | FitnesseElementType.DECISION_INPUT | FitnesseElementType.DECISION_OUTPUT | FitnesseElementType.QUERY_OUTPUT | FitnesseElementType.IMPORT =>
        List(CellBlock(node, rowId, nextCellId()))
      case FitnesseElementType.SCENARIO_NAME =>
        findInRow(node, rowId, nextCellId)
      case _ =>
        List(new LeafBlock(node))
    })
  }

  def calculateWidths(blocks: List[ASTBlock]): List[Int] = blocks match {
    case (barBlock @ BarBlock(bar)) :: (cellBlock: CellBlock) :: rest if bar.getElementType == FitnesseTokenType.TABLE_START =>
      (barBlock.width - TableFormatter.MIN_PADDING + cellBlock.width) :: calculateWidths(rest)
    case (cellBlock: CellBlock) :: rest =>
      cellBlock.width :: calculateWidths(rest)
    case (cellBlock: EmptyCellBarBlock) :: rest =>
      0 :: calculateWidths(rest) // compensate for leading space and " |" at end of block
    case _ :: rest => calculateWidths(rest)
    case Nil => Nil
  }

  lazy val cellBlocks: List[List[Int]] = splitBlocksByRow.map(row => calculateWidths(row))

  def splitBlocksByRow: List[List[ASTBlock]] = {
    TableBlock.groupPrefix(subBlocks)(_.getNode.getElementType == FitnesseTokenType.ROW_END)
  }

  lazy val tableFormatter: TableFormatter = new TableFormatter(cellBlocks.map(row => row.map(Integer.valueOf).asJava).asJava)

  def rightPadding(row: Int): Int = tableFormatter.rightPadding(row)

  def rightPadding(row: Int, col: Int): Int = tableFormatter.rightPadding(row, col)

  override def isLeaf: Boolean = subBlocks.isEmpty

  override def getSpacing(child1: Block, child2: Block): Spacing = {
    (child1, child2) match {
      case (_: BarBlock, _: CellBlock) =>
        createSpacing(TableFormatter.MIN_PADDING)
      case (_: EmptyCellBarBlock, _: CellBlock) =>
        createSpacing(TableFormatter.MIN_PADDING)
      case (CellBlock(cell, row, col), _) =>
        createSpacing(tableFormatter.rightPadding(row, col))
      case (_, EmptyCellBarBlock(bar, row, col)) =>
        createSpacing(tableFormatter.rightPadding(row, col) + TableFormatter.MIN_PADDING)
      case _ => null
    }
  }

  private def createSpacing(spacing: Int) = Spacing.createSpacing(spacing, spacing, 0, true, 0)


}

object TableBlock {

  /** Returns shortest possible list of lists xss such that
    *   - xss.flatten == xs
    *   - No sublist in xss contains an element matching p in its tail
    */
  def groupPrefix[T](xs: List[T])(p: T => Boolean): List[List[T]] = xs match {
    case Nil => Nil
    case xs =>
      xs span (!p(_)) match {
        case (ys, z :: zs) => (ys ::: List(z)) :: groupPrefix(zs)(p)
        case (ys, Nil) => List(ys)
      }

  }
}