package fitnesse.idea.lang.formatter

import com.intellij.formatting._
import com.intellij.lang.ASTNode
import fitnesse.idea.lang.parser.{FitnesseElementType, TableElementType}

class FitnesseBlock(node: ASTNode) extends BasicASTBlock(node) {

  override lazy val subBlocks: List[ASTBlock] = findSubBlocks(n => n.getElementType match {
    case _: TableElementType => List(new TableBlock(n))
    case FitnesseElementType.COLLAPSIBLE => List(new FitnesseBlock(n))
    case _ => List(new LeafBlock(n))
  })

  override def isLeaf: Boolean = getSubBlocks.isEmpty

}
