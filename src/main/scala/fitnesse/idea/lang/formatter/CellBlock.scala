package fitnesse.idea.lang.formatter

import com.intellij.formatting._
import com.intellij.lang.ASTNode

class CellBlock(node: ASTNode, alignment: Alignment) extends LeafBlock(node) {

  override def getAlignment: Alignment = alignment
}
