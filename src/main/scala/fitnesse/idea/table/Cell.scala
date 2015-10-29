package fitnesse.idea.table

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import fitnesse.idea.filetype.FitnesseLanguage

trait Cell extends PsiElement {
  def row = getParent.asInstanceOf[Row]

  def table = row.table

  def fixtureClass = table.fixtureClass

}

class SimpleCell(node: ASTNode) extends ASTWrapperPsiElement(node) with Cell

class CellElementType extends IElementType("CELL", FitnesseLanguage.INSTANCE) {
  override def toString = "Fitnesse:CELL"
}

object CellElementType {
  val INSTANCE = new CellElementType()
}