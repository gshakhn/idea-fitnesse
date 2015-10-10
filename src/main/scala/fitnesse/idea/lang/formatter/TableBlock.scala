package fitnesse.idea.lang.formatter

import java.util

import com.intellij.formatting._
import com.intellij.lang.ASTNode
import com.intellij.psi.tree.TokenSet
import fitnesse.idea.lang.parser.FitnesseElementType

import scala.collection.JavaConversions._

class TableBlock(node: ASTNode) extends BasicASTBlock(node) {

  val formatter = new FormatterImpl

  // TODO: Determine number of columns, so we can assign the right alignment to the right cell (cell/row end?)

  override lazy val getSubBlocks: util.List[Block] = findSubBlocks(n => n.getElementType match {
    case FitnesseElementType.ROW | FitnesseElementType.SCRIPT_ROW => new RowBlock(n)
    case _ => new LeafBlock(n)
  })

  override def isLeaf: Boolean = getSubBlocks.isEmpty
}

class RowBlock(node: ASTNode) extends BasicASTBlock(node) {
  val alignment = Alignment.createAlignment(true)

  override lazy val getSubBlocks: util.List[Block] = findSubBlocks(n => n.getElementType match {
    case FitnesseElementType.CELL => new CellBlock(n, alignment)
    case _ => new LeafBlock(n)
  })


  override def isLeaf: Boolean = getSubBlocks.isEmpty
}

class CellBlock(node: ASTNode, alignment: Alignment) extends LeafBlock(node) {

  override def getAlignment: Alignment = alignment
}
