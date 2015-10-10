package fitnesse.idea.lang.formatter

import java.util

import com.intellij.formatting._
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.impl.source.tree.FileElement
import com.intellij.psi.tree.TokenSet
import fitnesse.idea.lang.parser.{FitnesseElementType, TableElementType}

import scala.annotation.tailrec
import scala.collection.JavaConversions._

class FitnesseBlock(node: ASTNode) extends BasicASTBlock(node) {

  override lazy val getSubBlocks: util.List[Block] = findSubBlocks(n => n.getElementType match {
    case t: TableElementType => new TableBlock(n)
    case FitnesseElementType.COLLAPSIBLE => new FitnesseBlock(n)
    case _ => new LeafBlock(n)
  })

  override def isLeaf: Boolean = getSubBlocks.isEmpty
}
