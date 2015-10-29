package fitnesse.idea.formatter

import java.util

import com.intellij.formatting._
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiWhiteSpace
import fitnesse.idea.lexer.FitnesseTokenType
import fitnesse.idea.parser.FitnesseElementType

import scala.annotation.tailrec
import scala.collection.JavaConverters._

abstract class BasicASTBlock(node: ASTNode) extends ASTBlock {

  override def getNode: ASTNode = node

  /**
   * Returns the text range covered by the block.
   *
   * @return the text range.
   */
  override def getTextRange: TextRange = node.getTextRange

  def subBlocks: List[ASTBlock] = List.empty[ASTBlock]

  final override def getSubBlocks: util.List[Block] = subBlocks.asJava.asInstanceOf[util.List[Block]]

  /**
   * Checks if the current block is incomplete (contains elements that the user will
   * probably type but has not yet typed). For example, a parameter list is incomplete if
   * it does not have the trailing parenthesis, and a statement is incomplete if it does not
   * have the trailing semicolon. Used to determine the block for which {@link #getChildAttributes(int)}
   * is called when Enter is pressed: if the block immediately before the cursor is incomplete,
   * the method is called for that block; otherwise, the method is called for the parent of that block.
   *
   * @return true if the block is incomplete, false otherwise.
   */
  override def isIncomplete: Boolean = false

  /**
   * Returns the alignment and indent attributes which are applied to a new block inserted at
   * the specified position in the list of children of this block. Used for performing automatic
   * indent when Enter is pressed.
   *
   * @param newChildIndex the index where a new child is inserted.
   * @return the object containing the indent and alignment settings for the new child.
   */
  override def getChildAttributes(newChildIndex: Int): ChildAttributes = new ChildAttributes(Indent.getNoneIndent, null)

  /**
   * Returns a wrap object indicating the conditions under which a line break
   * is inserted before this block when formatting, if the block extends beyond the
   * right margin.
   *
   * @return the wrap object, or null if the line break is never inserted.
   * @see Wrap#createWrap(WrapType, boolean)
   * @see Wrap#createChildWrap(Wrap, WrapType, boolean)
   */
  override def getWrap: Wrap = null

  /**
   * Returns an indent object indicating how this block is indented relative
   * to its parent block.
   *
   * @return the indent object, or null if the default indent ("continuation without first") should be used.
   * @see com.intellij.formatting.Indent#getContinuationWithoutFirstIndent()
   */
  override def getIndent: Indent = Indent.getNoneIndent

  /**
   * Returns an alignment object indicating how this block is aligned with other blocks. Blocks
   * which return the same alignment object instance from the <code>getAlignment</code> method
   * are aligned with each other.
   *
   * @return the alignment object instance, or null if no alignment is required for the block.
   */
  override def getAlignment: Alignment = null

  /**
   * Returns a spacing object indicating what spaces and/or line breaks are added between two
   * specified children of this block.
   *
   * @param child1 the first child for which spacing is requested;
   *               <code>null</code> if given <code>'child2'</code> block is the first document block
   * @param child2 the second child for which spacing is requested.
   * @return the spacing instance, or null if no special spacing is required. If null is returned,
   *         the formatter does not insert or delete spaces between the child blocks, but may insert
   *         a line break if the line wraps at the position between the child blocks.
   * @see Spacing#createSpacing(int, int, int, boolean, int)
   * @see Spacing#getReadOnlySpacing()
   */
  override def getSpacing(child1: Block, child2: Block): Spacing = null

  def findSubBlocks(n: ASTNode, subBlockMatcher: (ASTNode, Option[ASTBlock]) => List[ASTBlock]): List[ASTBlock] = {
    @tailrec
    def collectBlocks(n: ASTNode, blocks: List[ASTBlock], toBlock: ((ASTNode, Option[ASTBlock]) => List[ASTBlock])): List[ASTBlock] = {
      n match {
        case null => blocks
        case _: PsiWhiteSpace => collectBlocks(n.getTreeNext, blocks, toBlock)
        case row if row.getElementType == FitnesseElementType.ROW || row.getElementType == FitnesseElementType.SCRIPT_ROW =>
          collectBlocks(n.getTreeNext, blocks ::: toBlock(n, blocks.lastOption), toBlock)
        case _ if n.getTextLength == 0 => collectBlocks(n.getTreeNext, blocks, toBlock)
        case _ => collectBlocks(n.getTreeNext, blocks ::: toBlock(n, blocks.lastOption), toBlock)
      }
    }
    collectBlocks(n.getFirstChildNode, List(), subBlockMatcher)
  }

  def findSubBlocks(subBlockMatcher: (ASTNode, Option[ASTBlock]) => List[ASTBlock]): List[ASTBlock] = {
    findSubBlocks(node, subBlockMatcher)
  }

  override def toString: String = s"${getClass.getSimpleName}:${node.getElementType}"

}
