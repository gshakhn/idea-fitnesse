package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import fitnesse.idea.fixtureclass.FixtureClass

class Row(node: ASTNode) extends ASTWrapperPsiElement(node) {

  // Todo: should be part of a TopRow class.
  def getFixtureClass = findChildByClass(classOf[FixtureClass]) match {
    case null => None
    case c => Some(c)
  }

  def getTable = getParent.asInstanceOf[Table]

  def getCells = findChildrenByClass(classOf[Cell])
}
