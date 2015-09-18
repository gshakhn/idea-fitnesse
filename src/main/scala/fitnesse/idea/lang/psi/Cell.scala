package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

trait Cell extends PsiElement {
  def row = getParent.asInstanceOf[Row]

  def table = row.table

  def fixtureClass = table.fixtureClass

}

class SimpleCell(node: ASTNode) extends ASTWrapperPsiElement(node) with Cell