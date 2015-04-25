package com.gshakhn.idea.idea.fitnesse.decisiontable

import com.gshakhn.idea.idea.fitnesse.lang.FitnesseLanguage
import com.gshakhn.idea.idea.fitnesse.lang.psi.{Table, FitnesseFile, PsiSuite}
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.{ElementManipulators, PsiClass, PsiMethod}
import org.mockito.Matchers.{any, anyBoolean, eq => m_eq}
import org.mockito.Mockito.when

class DecisionInputSuite extends PsiSuite {


  val myPsiClass = mock[PsiClass]
  val myPsiMethod = mock[PsiMethod]

  override protected def beforeAll(): Unit = {
    super.beforeAll()

    when(myPsiShortNamesCache.getClassesByName(m_eq("DecisionTable"), any[GlobalSearchScope])).thenReturn(Array(myPsiClass))
    when(myPsiClass.findMethodsByName(m_eq("setA"), anyBoolean)).thenReturn(Array(myPsiMethod))

    ElementManipulators.INSTANCE.addExplicitExtension(classOf[DecisionInput], new DecisionInputManipulator)
  }

  test("find references based on cell value") {
    val psiFile = myPsiFileFactory.createFileFromText(FitnesseLanguage.INSTANCE, "| decision table |\n| a | b | c |\n| 1 | 2 | 3 |");
    val table: Table = psiFile.getNode.getPsi(classOf[FitnesseFile]).getTables(0)
    assertResult("DecisionTable") {
      table.getFixtureClass.fixtureClassName
    }
    assertResult("setA") {
      table.getRows(1).getCells(0).asInstanceOf[DecisionInput].fixtureMethodName
    }

    assertResult(myPsiMethod) {
      val refs = table.getRows(1).getCells(0).asInstanceOf[DecisionInput].getReferences
      refs(0).resolve
    }
  }

  ignore("rename") {
    val psiFile = myPsiFileFactory.createFileFromText(FitnesseLanguage.INSTANCE, "| decision table |\n| a | b | c |\n| 1 | 2 | 3 |");
    val table: Table = psiFile.getNode.getPsi(classOf[FitnesseFile]).getTables(0)
    val cell = table.getRows(1).getCells(0).asInstanceOf[DecisionInput]
    val reference = cell.getReferences()(0)
    reference.handleElementRename("newName")

    assertResult("setNewName") {
      cell.fixtureMethodName
    }
  }
}
