package com.gshakhn.idea.idea.fitnesse.querytable

import com.gshakhn.idea.idea.fitnesse.decisiontable.{DecisionOutput, DecisionInputManipulator, DecisionInput}
import com.gshakhn.idea.idea.fitnesse.lang.FitnesseLanguage
import com.gshakhn.idea.idea.fitnesse.lang.psi.{PsiSuite, FitnesseFile, Table}
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.{ElementManipulators, PsiFile, PsiMethod, PsiClass}
import org.mockito.Matchers.{any, anyBoolean, eq => m_eq}
import org.mockito.Mockito.when

class QueryTableSuite extends PsiSuite {

  val myPsiClass = mock[PsiClass]
  val myPsiMethodSetA = mock[PsiMethod]
  val myPsiMethodTwoWords = mock[PsiMethod]

  var psiFile: PsiFile = null
  var table: Table = null

  override protected def beforeAll(): Unit = {
    super.beforeAll()

    when(myPsiShortNamesCache.getClassesByName(m_eq("QueryTable"), any[GlobalSearchScope])).thenReturn(Array(myPsiClass))
    when(myPsiClass.findMethodsByName(m_eq("setA"), anyBoolean)).thenReturn(Array(myPsiMethodSetA))
    when(myPsiClass.findMethodsByName(m_eq("twoWords"), anyBoolean)).thenReturn(Array(myPsiMethodTwoWords))

    ElementManipulators.INSTANCE.addExplicitExtension(classOf[DecisionInput], new DecisionInputManipulator)

    psiFile = myPsiFileFactory.createFileFromText(FitnesseLanguage.INSTANCE, "| query: query table |\n| a | b | two words |\n| 1 | 2 | 3 |");
    table = psiFile.getNode.getPsi(classOf[FitnesseFile]).getTables(0)
  }

  test("find table name") {
    assertResult("QueryTable") {
      table.getFixtureClass.fixtureClassName
    }
  }
  
  test("find query table query") {
    val output = table.getRows(1).getCells(2).asInstanceOf[QueryOutput]
    assertResult("twoWords") {
      output.fixtureMethodName
    }

    assertResult(myPsiMethodTwoWords) {
      val refs = output.getReferences
      refs(0).resolve
    }
  }

}
