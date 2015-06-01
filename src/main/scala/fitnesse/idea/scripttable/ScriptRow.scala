package fitnesse.idea.scripttable

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import fitnesse.idea.lang.lexer.FitnesseTokenType
import fitnesse.idea.lang.parser.FitnesseElementType
import fitnesse.idea.lang.psi.{FixtureClass, MethodReferences, Row}
import fitnesse.testsystems.slim.tables.Disgracer._

import scala.collection.JavaConversions._

class ScriptRow(node: ASTNode) extends Row(node) with MethodReferences {

  override def getFixtureClass: Option[FixtureClass] = getTable.getFixtureClass

  override def fixtureMethodName: String = {
    val snippets: java.util.List[PsiElement] = findChildrenByType(FitnesseElementType.CELL)
    val texts = snippets.map(_.getText.trim).toList

    def constructFixtureName(parts: List[String]) = {
      if (parts.isEmpty)
        ""
      else if (parts.head.endsWith(";"))
        disgraceMethodName(parts.head)
      else
        parts.view.zipWithIndex.filter(_._2 % 2 == 0).map(_._1).fold("") { (a, b) =>
          a + (if (a == "") disgraceMethodName(b) else disgraceClassName(b.capitalize))
          // TODO: make a string first, then disgrace it, like in the real code
        }
    }

    texts match {
      case ("check" | "check not") :: method => constructFixtureName(method.dropRight(1))
      case ("script" | "start" | "reject" | "ensure" | "show" | "note") :: method => constructFixtureName(method)
      case method => constructFixtureName(method)
    }
  }
}