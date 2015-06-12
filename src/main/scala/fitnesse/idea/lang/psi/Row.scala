package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import fitnesse.idea.fixtureclass.FixtureClass
import fitnesse.idea.scripttable.ScenarioName

trait Row extends PsiElement {

  def getTable = getParent.asInstanceOf[Table]

  def getFixtureClass: Option[FixtureClass]

  def getScenarioName: Option[ScenarioName]

  def getCells: List[Cell]
}


class SimpleRow(node: ASTNode) extends ASTWrapperPsiElement(node) with Row {

  // Todo: should be part of a TopRow class??
  override def getFixtureClass = Option(findChildByClass(classOf[FixtureClass]))

  override def getScenarioName: Option[ScenarioName] = Option(findChildByClass(classOf[ScenarioName]))

  def getCells = findChildrenByClass(classOf[Cell]).toList
}
