package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

trait Cell extends PsiElement {
  def getRow = getParent.asInstanceOf[Row]

  def getTable = getRow.getTable

  def getFixtureClass = getTable.getFixtureClass

}

class SimpleCell(node: ASTNode) extends ASTWrapperPsiElement(node) with Cell {

}