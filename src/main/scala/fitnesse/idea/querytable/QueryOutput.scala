package fitnesse.idea.querytable

import com.intellij.lang.ASTNode
import fitnesse.idea.lang.psi.{Cell, MethodReferences}

class QueryOutput(node: ASTNode) extends Cell(node) with MethodReferences {

  override def fixtureMethodName = "query"

}