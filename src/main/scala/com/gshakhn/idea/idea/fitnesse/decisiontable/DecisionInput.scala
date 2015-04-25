package com.gshakhn.idea.idea.fitnesse.decisiontable

import com.gshakhn.idea.idea.fitnesse.lang.psi.{MethodReferences, Cell}
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiMethod
import fitnesse.testsystems.slim.tables.Disgracer.disgraceClassName

class DecisionInput(node: ASTNode) extends Cell(node) with MethodReferences {

  override def fixtureMethodName = "set" + disgraceClassName(node.getText)

  override def createReference(psiMethod: PsiMethod) = new DecisionInputReference(psiMethod, this)
}