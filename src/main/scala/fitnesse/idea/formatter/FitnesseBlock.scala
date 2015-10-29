package fitnesse.idea.formatter

import com.intellij.formatting._
import com.intellij.lang.ASTNode
import fitnesse.idea.parser.{FitnesseElementType, TableElementType}

class FitnesseBlock(node: ASTNode) extends BasicASTBlock(node) {

  override lazy val subBlocks: List[ASTBlock] = findSubBlocks((node, previous) => node.getElementType match {
    case _: TableElementType => List(new TableBlock(node))
    case FitnesseElementType.COLLAPSIBLE => List(new FitnesseBlock(node))
    case _ => List(new LeafBlock(node))
  })

  override def isLeaf: Boolean = getSubBlocks.isEmpty

}
