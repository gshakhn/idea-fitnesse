package fitnesse.idea.querytable

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.{PsiElement, PsiType}
import fitnesse.idea.fixturemethod.ReturnType.ReturnType
import fitnesse.idea.fixturemethod.ReturnType.ReturnType
import fitnesse.idea.fixturemethod.{ReturnType, FixtureMethod, MethodReference}
import fitnesse.idea.table.Cell

class QueryOutput(node: ASTNode) extends ASTWrapperPsiElement(node) with Cell with FixtureMethod {

  override val fixtureMethodName = "query"

  override def parameters: List[String] = Nil

  override def getReference = new MethodReference(this)

  override def returnType: ReturnType = ReturnType.List

  override def getName: String = fixtureMethodName

  override def setName(newName: String): PsiElement = this
}