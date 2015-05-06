package fitnesse.idea.scripttable

import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.{PsiClass, PsiMethod}
import fitnesse.idea.lang.FitnesseLanguage
import fitnesse.idea.lang.psi.{FitnesseFile, PsiSuite}
import org.mockito.Matchers.{any, anyBoolean, eq => m_eq}
import org.mockito.Mockito.when

class ScriptTableSuite extends PsiSuite {

  val myPsiClass = mock[PsiClass]
  val myPsiMethodTwoWords = mock[PsiMethod]

  override protected def beforeAll(): Unit = {
    super.beforeAll()

    when(myPsiShortNamesCache.getClassesByName(m_eq("MyScriptTable"), any[GlobalSearchScope])).thenReturn(Array(myPsiClass))

  }

  def scriptRow(s: String): ScriptRow = {
    val psiFile = myPsiFileFactory.createFileFromText(FitnesseLanguage.INSTANCE,
      "| script:MyScriptTable|\n" + s)
    psiFile.getNode.getPsi(classOf[FitnesseFile]).getTables(0).getRows(1).asInstanceOf[ScriptRow]
  }

  test("find table name") {
    val psiFile = myPsiFileFactory.createFileFromText(FitnesseLanguage.INSTANCE,
      "| script:MyScriptTable|")
    val table = psiFile.getNode.getPsi(classOf[FitnesseFile]).getTables(0)

    assertResult("MyScriptTable") {
      table.getFixtureClass.get.fixtureClassName.get
    }
  }
  
  test("find script table ensure method") {
    when(myPsiClass.findMethodsByName(m_eq("twoWords"), anyBoolean)).thenReturn(Array(myPsiMethodTwoWords))

    val output = scriptRow("| ensure | two | 3 | words |")
    assertResult("twoWords") {
      output.fixtureMethodName
    }

    assertResult(myPsiMethodTwoWords) {
      val refs = output.getReferences
      refs(0).resolve
    }
  }

  test("find script table method") {
    val output = scriptRow("| method invocation | foo |")
    assertResult("methodInvocation") {
      output.fixtureMethodName
    }
  }

  test("method name for sequencial function call") {
    val output = scriptRow("| method invocation; | foo | bar |")
    assertResult("methodInvocation") {
      output.fixtureMethodName
    }
  }

  test("method name for check row") {
    val output = scriptRow("| check | method invocation | foo | bar |")
    assertResult("methodInvocation") {
      output.fixtureMethodName
    }
  }

  test("method name for check-not row") {
    val output = scriptRow("| check not | method invocation | foo | bar |")
    assertResult("methodInvocation") {
      output.fixtureMethodName
    }
  }

  test("method with keyword, name absent") {
    val output = scriptRow("| ensure |")
    assertResult("") {
      output.fixtureMethodName
    }
  }

  test("method with check keyword, name absent") {
    val output = scriptRow("| check |")
    assertResult("") {
      output.fixtureMethodName
    }
  }

  test("method name absent") {
    val output = scriptRow("| |")
    assertResult("") {
      output.fixtureMethodName
    }
  }

}
