package com.gshakhn.idea.idea.fitnesse.scripttable

import com.gshakhn.idea.idea.fitnesse.lang.lexer.FitnesseTokenType
import com.gshakhn.idea.idea.fitnesse.lang.psi.{MethodReferences, Row}
import com.intellij.lang.ASTNode
import com.intellij.psi.{PsiElement, PsiReference, PsiMethod}
import scala.collection.JavaConversions._
import fitnesse.testsystems.slim.tables.Disgracer._

class ScriptRow(node: ASTNode) extends Row(node) with MethodReferences {

  override def fixtureClassName: String = getTable.getFixtureClass.fixtureClassName

  override def fixtureMethodName: String = {
    val snippets: java.util.List[PsiElement] = findChildrenByType(FitnesseTokenType.CELL_TEXT)
    val texts = snippets.map(_.getText.trim).toList

    def constructFixtureName(parts: List[String]) = {
      if (parts.isEmpty)
        ""
      else if (parts.head.endsWith(";"))
        disgraceMethodName(parts.head)
      else
        parts.view.zipWithIndex.filter(_._2 % 2 == 0).map(_._1).fold("") { (a, b) =>
          a + (if (a == "") disgraceMethodName(b) else disgraceClassName(b.capitalize))
        }
    }

    texts match {
      case ("check" | "check not") :: method => constructFixtureName(method.dropRight(1))
      case ("script" | "start" | "reject" | "ensure" | "show" | "note") :: method => constructFixtureName(method)
      case method => constructFixtureName(method)
    }
  }

  override def createReference(psiMethod: PsiMethod): PsiReference = new ScriptRowReference(psiMethod, this)
  
}
