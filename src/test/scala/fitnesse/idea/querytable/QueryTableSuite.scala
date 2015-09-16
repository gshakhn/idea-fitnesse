package fitnesse.idea.querytable

import com.intellij.psi._
import com.intellij.psi.search.GlobalSearchScope
import fitnesse.idea.lang.psi.{PsiSuite, Table}
import org.mockito.Matchers.{any, anyBoolean, eq => m_eq}
import org.mockito.Mockito.when

class QueryTableSuite extends PsiSuite {

  val myPsiClass = mock[PsiClass]
  val myPsiMethodQuery = mock[PsiMethod]

  var table: Table = null
  var output: QueryOutput = null

  override protected def beforeAll(): Unit = {
    super.beforeAll()

    when(myPsiShortNamesCache.getClassesByName(m_eq("QueryTable"), any[GlobalSearchScope])).thenReturn(Array(myPsiClass))
    when(myPsiClass.findMethodsByName(m_eq("query"), anyBoolean)).thenReturn(Array(myPsiMethodQuery))

    table = createTable("| query: query table |\n| a | b | two words |\n| 1 | 2 | 3 |")
    output = table.getRows(1).getCells(2).asInstanceOf[QueryOutput]
  }

  test("find table name") {
    assertResult("QueryTable") {
      table.getFixtureClass.get.fixtureClassName.get
    }
  }
  
  test("find query table query") {
    assertResult("query") {
      output.fixtureMethodName
    }

    assertResult(myPsiMethodQuery) {
      val refs = output.getReferences
      refs(0).resolve
    }
  }

  test("parameters for query table query") {
    assertResult(Nil) {
      output.parameters
    }
  }

  test("return type for query table query") {
    assertResult(psiClassType("java.util.List")) {
      output.returnType
    }
  }

}
