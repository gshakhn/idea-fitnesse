package fitnesse.idea.querytable

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import fitnesse.idea.fixturemethod.MethodReferences
import fitnesse.idea.lang.psi.Cell

class QueryOutput(node: ASTNode) extends ASTWrapperPsiElement(node) with Cell with MethodReferences {

  override def fixtureMethodName = "query"

}