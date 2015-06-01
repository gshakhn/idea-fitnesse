package fitnesse.idea.decisiontable

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiMethod
import fitnesse.idea.lang.psi.{Cell, MethodReferences}
import fitnesse.testsystems.slim.tables.Disgracer.disgraceMethodName

class DecisionInput(node: ASTNode) extends Cell(node) with MethodReferences {

  override def fixtureMethodName = disgraceMethodName("set " + node.getText.trim)

  override def createReference(psiMethod: PsiMethod) = new DecisionInputReference(psiMethod, this)
}