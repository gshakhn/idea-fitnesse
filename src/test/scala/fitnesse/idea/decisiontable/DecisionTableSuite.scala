package fitnesse.idea.decisiontable

import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.{ElementManipulators, PsiClass, PsiFile, PsiMethod}
import fitnesse.idea.lang.FitnesseLanguage
import fitnesse.idea.lang.psi.{FitnesseFile, PsiSuite, Table}
import fitnesse.testsystems.slim.tables.Disgracer._
import org.mockito.Matchers.{any, anyBoolean, eq => m_eq}
import org.mockito.Mockito.when

class DecisionTableSuite extends PsiSuite {


  val myPsiClass = mock[PsiClass]
  val myPsiMethodSetA = mock[PsiMethod]
  val myPsiMethodC = mock[PsiMethod]
  val myPsiMethodFancyLongName = mock[PsiMethod]
  val myPsiMethodFancyQueryName = mock[PsiMethod]

  var psiFile: PsiFile = null
  var table: Table = null
  
  override protected def beforeAll(): Unit = {
    super.beforeAll()

    when(myPsiShortNamesCache.getClassesByName(m_eq("DecisionTable"), any[GlobalSearchScope])).thenReturn(Array(myPsiClass))
    when(myPsiClass.findMethodsByName(m_eq("setA"), anyBoolean)).thenReturn(Array(myPsiMethodSetA))
    when(myPsiClass.findMethodsByName(m_eq("c"), anyBoolean)).thenReturn(Array(myPsiMethodC))
    when(myPsiClass.findMethodsByName(m_eq("setFancyLongName"), anyBoolean)).thenReturn(Array(myPsiMethodFancyLongName))
    when(myPsiClass.findMethodsByName(m_eq("fancyQueryName"), anyBoolean)).thenReturn(Array(myPsiMethodFancyQueryName))

    ElementManipulators.INSTANCE.addExplicitExtension(classOf[DecisionInput], new DecisionInputManipulator)

    psiFile = myPsiFileFactory.createFileFromText(FitnesseLanguage.INSTANCE, "| decision table |\n| a | b | c? | fancy long name | fancy query name? |\n| 1 | 2 | 3 |")
    table = psiFile.getNode.getPsi(classOf[FitnesseFile]).getTables(0)
  }

  test("find table name") {
    assertResult("DecisionTable") {
      table.getFixtureClass.get.fixtureClassName.get
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

  test("handle graceful names") {
    val output = table.getRows(1).getCells(4).asInstanceOf[DecisionOutput]
    assertResult("fancyQueryName") {
      output.fixtureMethodName
    }

    assertResult(myPsiMethodFancyQueryName) {
      val refs = output.getReferences
      refs(0).resolve
    }
  }

}
