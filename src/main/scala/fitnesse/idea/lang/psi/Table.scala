package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import fitnesse.idea.fixtureclass.FixtureClass
import fitnesse.idea.scripttable.ScenarioName

class Table(node: ASTNode) extends ASTWrapperPsiElement(node) {

  def rows = findChildrenByClass(classOf[Row])

  def fixtureClass: Option[FixtureClass] = findInFirstRow(classOf[FixtureClass])

  def scenarioName: Option[ScenarioName] = findInFirstRow(classOf[ScenarioName])

  private def findInFirstRow[T](clazz: Class[T]): Option[T] = Option(rows(0).findInRow(clazz))
}
