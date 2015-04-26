package com.gshakhn.idea.idea.fitnesse.decisiontable

import com.gshakhn.idea.idea.fitnesse.lang.FitnesseLanguage
import com.gshakhn.idea.idea.fitnesse.lang.psi.{MethodReferences, Table, FitnesseFile, PsiSuite}
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.{PsiFile, ElementManipulators, PsiClass, PsiMethod}
import org.mockito.Matchers.{any, anyBoolean, eq => m_eq}
import org.mockito.Mockito.when

class DecisionTableSuite extends PsiSuite {


  val myPsiClass = mock[PsiClass]
  val myPsiMethodSetA = mock[PsiMethod]
  val myPsiMethodC = mock[PsiMethod]

  var psiFile: PsiFile = null
  var table: Table = null
  
  override protected def beforeAll(): Unit = {
    super.beforeAll()

    when(myPsiShortNamesCache.getClassesByName(m_eq("DecisionTable"), any[GlobalSearchScope])).thenReturn(Array(myPsiClass))
    when(myPsiClass.findMethodsByName(m_eq("setA"), anyBoolean)).thenReturn(Array(myPsiMethodSetA))
    when(myPsiClass.findMethodsByName(m_eq("c"), anyBoolean)).thenReturn(Array(myPsiMethodC))

    ElementManipulators.INSTANCE.addExplicitExtension(classOf[DecisionInput], new DecisionInputManipulator)

    psiFile = myPsiFileFactory.createFileFromText(FitnesseLanguage.INSTANCE, "| decision table |\n| a | b | c? |\n| 1 | 2 | 3 |");
    table = psiFile.getNode.getPsi(classOf[FitnesseFile]).getTables(0)
  }

  test("find table name") {
    assertResult("DecisionTable") {
      table.getFixtureClass.fixtureClassName
    }
  }

  test("find decision table setter") {
    val input = table.getRows(1).getCells(0).asInstanceOf[DecisionInput]
    assertResult("setA") {
      input.fixtureMethodName
    }

    assertResult(myPsiMethodSetA) {
      val refs = input.getReferences
      refs(0).resolve
    }
  }

  test("find decision table query") {
    val output = table.getRows(1).getCells(2).asInstanceOf[DecisionOutput]
    assertResult("c") {
      output.fixtureMethodName
    }

    assertResult(myPsiMethodC) {
      val refs = output.getReferences
      refs(0).resolve
    }
  }

}
