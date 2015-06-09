package fitnesse.idea.decisiontable

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import fitnesse.idea.fixturemethod.MethodReferences
import fitnesse.idea.lang.psi.Cell
import fitnesse.testsystems.slim.tables.Disgracer._

class DecisionOutput(node: ASTNode) extends ASTWrapperPsiElement(node) with Cell with MethodReferences {

  override def fixtureMethodName = disgraceMethodName(node.getText.trim)

}