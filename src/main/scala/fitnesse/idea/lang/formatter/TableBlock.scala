package fitnesse.idea.lang.formatter

import java.util

import com.intellij.formatting._
import com.intellij.lang.ASTNode
import fitnesse.idea.lang.parser.FitnesseElementType

import scala.collection.JavaConversions._

class TableBlock(node: ASTNode) extends BasicASTBlock(node) {

  val formatter = new FormatterImpl

  val subBlocks: List[Block] = findSubBlocks(n => n.getElementType match {
    case FitnesseElementType.ROW | FitnesseElementType.SCRIPT_ROW => new FitnesseBlock(n)
    case _ => new LeafBlock(n)
  })

  /**
   * Returns the list of child blocks for the specified block. <b>Important</b>: The same list
   * of blocks must be returned when <code>getSubBlocks()</code> is repeatedly called on a particular
   * <code>Block</code> instance.
   *
   * @return the child block list.
   * @see #isLeaf()
   */
  override def getSubBlocks: util.List[Block] = subBlocks // return tables here

  /**
   * Returns true if the specified block may not contain child blocks. Used as an optimization
   * to avoid building the complete formatting model through calls to {@link #getSubBlocks()}.
   *
   * @return true if the block is a leaf block and may not contain child blocks, false otherwise.
   */
  override def isLeaf: Boolean = subBlocks.isEmpty
}
