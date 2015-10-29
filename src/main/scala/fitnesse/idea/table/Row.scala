package fitnesse.idea.table

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

trait Row extends PsiElement {

  def table = getParent.asInstanceOf[Table]

  def cells: List[Cell]

  // Make this method public (it's protected in PsiElement,
  // hence Scala visibility rules prevent us from accessing it
  def findInRow[T](clazz: Class[T]): T
}


class SimpleRow(node: ASTNode) extends ASTWrapperPsiElement(node) with Row {

  def cells = findChildrenByClass(classOf[Cell]).toList

  override def findInRow[T](clazz: Class[T]): T = super.findChildByClass(clazz)

}
