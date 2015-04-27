package com.gshakhn.idea.idea.fitnesse.decisiontable

import com.gshakhn.idea.idea.fitnesse.lang.psi.{Cell, MethodReferences}
import com.intellij.lang.ASTNode
import fitnesse.testsystems.slim.tables.Disgracer._

class DecisionOutput(node: ASTNode) extends Cell(node) with MethodReferences {

  override def fixtureMethodName = disgraceMethodName(node.getText.trim)

}