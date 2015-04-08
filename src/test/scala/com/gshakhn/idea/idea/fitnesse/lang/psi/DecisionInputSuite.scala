package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.gshakhn.idea.idea.fitnesse.lang.FitnesseLanguage
import com.gshakhn.idea.idea.fitnesse.lang.manipulator.DecisionInputManipulator
import com.gshakhn.idea.idea.fitnesse.lang.parser.TableElementType
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.{ElementManipulators, PsiMethod, PsiClass}
import org.mockito.Matchers.{anyBoolean, any, eq => m_eq}
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
    var psiFile = myPsiFileFactory.createFileFromText(FitnesseLanguage.INSTANCE, "| decision table |\n| a | b | c |\n| 1 | 2 | 3 |");
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

  test("rename") {
    var psiFile = myPsiFileFactory.createFileFromText(FitnesseLanguage.INSTANCE, "| decision table |\n| a | b | c |\n| 1 | 2 | 3 |");
    val table: Table = psiFile.getNode.getPsi(classOf[FitnesseFile]).getTables(0)
    val cell = table.getRows(1).getCells(0).asInstanceOf[DecisionInput]
    val reference = cell.getReferences()(0)
    reference.handleElementRename("newName")

    assertResult("setNewName") {
      cell.fixtureMethodName
    }

  }
}
