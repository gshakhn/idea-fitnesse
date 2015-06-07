package fitnesse.idea.scripttable

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import fitnesse.idea.fixtureclass.FixtureClass
import fitnesse.idea.fixturemethod.MethodReferences
import fitnesse.idea.lang.parser.FitnesseElementType
import fitnesse.idea.lang.psi.{Cell, Table, Row}
import fitnesse.testsystems.slim.tables.Disgracer._

import scala.collection.JavaConversions._

class ScriptRow(node: ASTNode) extends ASTWrapperPsiElement(node) with Row with MethodReferences {

  override def fixtureMethodName: String = {
    val snippets = getCells
    val texts = getCells.map(_.getText.trim)

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

  override def getCells: List[Cell] = findChildrenByType(FitnesseElementType.CELL).toList
}