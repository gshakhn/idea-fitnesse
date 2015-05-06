package fitnesse.idea.querytable

import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.{PsiClass, PsiFile, PsiMethod}
import fitnesse.idea.lang.FitnesseLanguage
import fitnesse.idea.lang.psi.{FitnesseFile, PsiSuite, Table}
import org.mockito.Matchers.{any, anyBoolean, eq => m_eq}
import org.mockito.Mockito.when

class QueryTableSuite extends PsiSuite {

  val myPsiClass = mock[PsiClass]
  val myPsiMethodQuery = mock[PsiMethod]

  var psiFile: PsiFile = null
  var table: Table = null

  override protected def beforeAll(): Unit = {
    super.beforeAll()

    when(myPsiShortNamesCache.getClassesByName(m_eq("QueryTable"), any[GlobalSearchScope])).thenReturn(Array(myPsiClass))
    when(myPsiClass.findMethodsByName(m_eq("query"), anyBoolean)).thenReturn(Array(myPsiMethodQuery))

    psiFile = myPsiFileFactory.createFileFromText(FitnesseLanguage.INSTANCE, "| query: query table |\n| a | b | two words |\n| 1 | 2 | 3 |")
    table = psiFile.getNode.getPsi(classOf[FitnesseFile]).getTables(0)
  }

  test("find table name") {
    assertResult("QueryTable") {
      table.getFixtureClass.get.fixtureClassName.get
    }
  }
  
  test("find query table query") {
    val output = table.getRows(1).getCells(2).asInstanceOf[QueryOutput]
    assertResult("query") {
      output.fixtureMethodName
    }

    assertResult(myPsiMethodQuery) {
      val refs = output.getReferences
      refs(0).resolve
    }
  }

}
