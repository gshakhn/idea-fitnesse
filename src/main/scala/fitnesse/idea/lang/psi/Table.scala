package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

abstract class Table(node: ASTNode) extends ASTWrapperPsiElement(node) {
  def getRows = findChildrenByClass(classOf[Row])

  def getFixtureClass = getRows(0).getFixtureClass
}
