package fitnesse.idea.querytable

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.{PsiElement, PsiType}
import fitnesse.idea.fixturemethod.{FixtureMethod, MethodReference}
import fitnesse.idea.table.Cell

class QueryOutput(node: ASTNode) extends ASTWrapperPsiElement(node) with Cell with FixtureMethod {

  override val fixtureMethodName = "query"

  override def parameters: List[String] = Nil

  override def getReference = new MethodReference(this)

  override def returnType: PsiType = PsiType.getTypeByName("java.util.List", getProject, getResolveScope)

  override def getName: String = fixtureMethodName

  // Update ASTNode instead?
  override def setName(newName: String): PsiElement = {
    //    val newElement = FixtureClassElementType.createFixtureClass(getProject, newName)
    //    this.replace(newElement)
    //    newElement
    this
  }
}