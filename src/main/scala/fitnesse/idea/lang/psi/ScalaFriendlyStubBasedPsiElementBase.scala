package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.{StubBasedPsiElement, PsiElement}
import com.intellij.psi.impl.source.tree.{TreeElementVisitor, TreeElement}
import com.intellij.psi.stubs.{IStubElementType, StubElement}
import com.intellij.psi.tree.{TokenSet, IElementType}
import fitnesse.idea.lang.FitnesseLanguage


trait ScalaFriendlyStubBasedPsiElementBase[T <: StubElement[_ <: PsiElement]] {
  this: StubBasedPsiElementBase[T] =>

  protected object DispatchType extends Enumeration {
    val STUB = Value("STUB")
    val NODE = Value("NODE")
  }

  protected val STUB = DispatchType.STUB
  protected val NODE = DispatchType.NODE

  def source = if (getStub != null) DispatchType.STUB else DispatchType.NODE


  override def getElementType: IStubElementType[_ <: StubElement[_ <: PsiElement], _ <: PsiElement] = source match {
    case STUB => getStub.getStubType
    case NODE => getNode.getElementType.asInstanceOf[IStubElementType[_ <: StubElement[_ <: PsiElement], _ <: PsiElement]]
  }
}

