package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

trait Cell extends PsiElement {
  def getRow = getParent.asInstanceOf[Row]

  def getFixtureClass = getRow.getTable.getFixtureClass

  def fixtureClassName: Option[String] = getRow.getTable.getFixtureClass match {
    case Some(cls) => cls.fixtureClassName
    case None => None
  }

}

class SimpleCell(node: ASTNode) extends ASTWrapperPsiElement(node) with Cell {

}