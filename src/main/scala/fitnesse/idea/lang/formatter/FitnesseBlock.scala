package fitnesse.idea.lang.formatter

import java.util

import com.intellij.formatting._
import com.intellij.lang.ASTNode
import fitnesse.idea.lang.parser.{FitnesseElementType, TableElementType}

import scala.collection.JavaConverters._

class FitnesseBlock(node: ASTNode) extends BasicASTBlock(node) {

  override lazy val getSubBlocks: util.List[Block] = findSubBlocks(n => n.getElementType match {
    case _: TableElementType => new TableBlock(n)
    case FitnesseElementType.COLLAPSIBLE => new FitnesseBlock(n)
    case _ => new LeafBlock(n)
  }).asJava

  override def isLeaf: Boolean = getSubBlocks.isEmpty

}
