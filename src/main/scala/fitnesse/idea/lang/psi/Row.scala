package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import fitnesse.idea.fixtureclass.FixtureClass

trait Row extends PsiElement {

  def getTable = getParent.asInstanceOf[Table]

  def getFixtureClass: Option[FixtureClass] = getTable.getFixtureClass

  def getCells: List[Cell]
}


class SimpleRow(node: ASTNode) extends ASTWrapperPsiElement(node) with Row {

  // Todo: should be part of a TopRow class.
  override def getFixtureClass = findChildByClass(classOf[FixtureClass]) match {
    case null => None
    case c => Some(c)
  }

  def getCells = findChildrenByClass(classOf[Cell]).toList
}
