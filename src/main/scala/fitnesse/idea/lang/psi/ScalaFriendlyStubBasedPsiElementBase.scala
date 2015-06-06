package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.{TreeElementVisitor, TreeElement}
import com.intellij.psi.stubs.{IStubElementType, StubElement}
import com.intellij.psi.tree.{TokenSet, IElementType}
import fitnesse.idea.lang.FitnesseLanguage

/**
 * Work around the constructors of StubBasedPsiElementBase. Scala is a little more restricted in that in can not call
 * different parent constructors.
 *
 * Usage example:
 *
 *   def this(node: ASTNode) = { this(); init(node) }
 *   def this(stub: T) = { this(); init(stub) }
 *
 * @tparam T
 */
abstract class ScalaFriendlyStubBasedPsiElementBase[T <: StubElement[_ <: PsiElement]] extends StubBasedPsiElementBase[T](DummyASTNode.getInstanceForJava) {

  protected def init(node: ASTNode) = setNode(node)
  protected def init(stub: T) = { setStub(stub); setNode(null) }

  override def getElementType: IStubElementType[_ <: StubElement[_ <: PsiElement], _ <: PsiElement] = {
    if (getStub != null) getStub.getStubType
    else getNode.getElementType.asInstanceOf[IStubElementType[_ <: StubElement[_ <: PsiElement], _ <: PsiElement]]
  }
}

/*
 * Taken from: intellij-scala/src/org/jetbrains/plugins/scala/lang/psi/stubs/elements/wrappers/DummyASTNode.scala
 */
private object DummyASTNode extends TreeElement(new IElementType("DummyASTNode", FitnesseLanguage.INSTANCE)) {
  def getText: String = null
  def removeRange(firstNodeToRemove: ASTNode, firstNodeToKeep: ASTNode) {}
  def replaceChild(oldChild: ASTNode, newChild: ASTNode) {}
  override def toString: String = "Dummy AST node"
  def addChild(child: ASTNode) {}
  def textContains(c: Char): Boolean = false
  def replaceAllChildrenToChildrenOf(anotherParent: ASTNode) {}
  def addChild(child: ASTNode, anchorBefore: ASTNode) {}
  def getTextLength: Int = 42
  def getChildren(filter: TokenSet) = Array[ASTNode]()
  def addLeaf(leafType: IElementType, leafText: CharSequence, anchorBefore: ASTNode) {}
  def removeChild(child: ASTNode) {}
  def addChildren(firstChild: ASTNode, firstChildToNotAdd: ASTNode, anchorBefore: ASTNode) {}
  def findChildByType(typesSet: TokenSet) = null
  def findChildByType(elem: IElementType) = null
  def findChildByType(elem: IElementType, anchor: ASTNode) = null
  def findChildByType(typesSet: TokenSet, anchor: ASTNode) = null
  def findLeafElementAt(offset: Int) = null
  def textToCharArray = new Array[Char](42)
  def getLastChildNode = null
  def getFirstChildNode = null
  def hc: Int = 42
  def getPsi = null
  def acceptTree(visitor: TreeElementVisitor) {}
  def getCachedLength: Int = 42
  def textMatches(buffer: CharSequence, start: Int): Int = -1
  def getNotCachedLength: Int = 42
  def getChars: CharSequence = textToCharArray.mkString
  def getPsi[T <: PsiElement](p1: Class[T]): T = null.asInstanceOf[T]

  def getInstanceForJava = this
}