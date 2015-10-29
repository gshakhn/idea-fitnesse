package fitnesse.idea.table

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import fitnesse.idea.fixtureclass.FixtureClass

class Table(node: ASTNode) extends ASTWrapperPsiElement(node) {

  def rows = findChildrenByClass(classOf[Row])

  def fixtureClass: Option[FixtureClass] = findInFirstRow(classOf[FixtureClass])

  def findInFirstRow[T](clazz: Class[T]): Option[T] = Option(rows(0).findInRow(clazz))
}
