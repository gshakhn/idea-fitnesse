package fitnesse.idea.lang.formatter

import com.intellij.lang.ASTNode

class LeafBlock(node: ASTNode) extends BasicASTBlock(node) {

  lazy val width: Integer = node.getText.trim.length

  override def isLeaf: Boolean = true
}
