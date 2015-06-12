package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.impl.PsiElementBase
import fitnesse.idea.fixtureclass.FixtureClass
import fitnesse.idea.scripttable.ScenarioName

abstract class Table(node: ASTNode) extends ASTWrapperPsiElement(node) {

  def getRows = findChildrenByClass(classOf[Row])

  def getFixtureClass: Option[FixtureClass] = findInFirstRow(classOf[FixtureClass])

  def getScenarioName: Option[ScenarioName] = findInFirstRow(classOf[ScenarioName])

  private def findInFirstRow[T](clazz: Class[T]): Option[T] = Option(getRows(0).findChildByClass(clazz))
}
