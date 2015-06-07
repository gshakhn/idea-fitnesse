package fitnesse.idea.scripttable

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import fitnesse.idea.lang.psi.Cell

class ScenarioName(node: ASTNode) extends ASTWrapperPsiElement(node) with Cell {

}