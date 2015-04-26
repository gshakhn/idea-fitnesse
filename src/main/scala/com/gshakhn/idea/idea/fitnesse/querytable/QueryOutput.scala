package com.gshakhn.idea.idea.fitnesse.querytable

import com.gshakhn.idea.idea.fitnesse.lang.psi.{Cell, MethodReferences}
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiMethod
import fitnesse.testsystems.slim.tables.Disgracer._

class QueryOutput(node: ASTNode) extends Cell(node) with MethodReferences {

  override def fixtureMethodName = disgraceMethodName(node.getText.trim)

  override def createReference(psiMethod: PsiMethod) = new QueryOutputReference(psiMethod, this)
}