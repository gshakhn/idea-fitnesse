package com.gshakhn.idea.idea.fitnesse.querytable

import com.gshakhn.idea.idea.fitnesse.lang.psi.{Cell, MethodReferences}
import com.intellij.lang.ASTNode

class QueryOutput(node: ASTNode) extends Cell(node) with MethodReferences {

  override def fixtureMethodName = "query"

}