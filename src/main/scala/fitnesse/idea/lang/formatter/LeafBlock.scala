package fitnesse.idea.lang.formatter

import java.util

import com.intellij.formatting._
import com.intellij.lang.ASTNode

import scala.collection.JavaConversions._

class LeafBlock(node: ASTNode) extends BasicASTBlock(node) {

  override def getSubBlocks: util.List[Block] = List.empty[Block]

  override def isLeaf: Boolean = true
}
