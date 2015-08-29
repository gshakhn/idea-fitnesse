package fitnesse.idea.querytable

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import fitnesse.idea.fixturemethod.{MethodReference, FixtureMethod}
import fitnesse.idea.lang.psi.Cell

class QueryOutput(node: ASTNode) extends ASTWrapperPsiElement(node) with Cell with FixtureMethod {

  override def fixtureMethodName = "query"

  override def getReference = new MethodReference(this)
}