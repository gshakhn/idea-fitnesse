package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import fitnesse.idea.fixtureclass.FixtureClass
import fitnesse.idea.scripttable.ScenarioName

trait Row extends PsiElement {

  def getTable = getParent.asInstanceOf[Table]

  def getCells: List[Cell]

  // Make this method public (it's protected in PsiElement,
  // hence Scala visibility rules prevent us from accessing it
  def findChildByClass[T](clazz: Class[T]): T
}


class SimpleRow(node: ASTNode) extends ASTWrapperPsiElement(node) with Row {

  def getCells = findChildrenByClass(classOf[Cell]).toList

  override def findChildByClass[T](clazz: Class[T]): T = super.findChildByClass(clazz)

}
