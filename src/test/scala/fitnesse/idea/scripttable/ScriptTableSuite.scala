package fitnesse.idea.scripttable

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.{PsiReference, PsiClass, PsiMethod}
import fitnesse.idea.lang.FitnesseLanguage
import fitnesse.idea.lang.psi.{SimpleRow, FitnesseFile, PsiSuite}
import org.mockito.Matchers.{any, anyBoolean, eq => m_eq}
import org.mockito.Mockito.when

import scala.collection.JavaConverters._

class ScriptTableSuite extends PsiSuite {

  val myPsiClass = mock[PsiClass]
  val myPsiMethodTwoWords = mock[PsiMethod]

  override protected def beforeAll(): Unit = {
    super.beforeAll()

    when(myPsiShortNamesCache.getClassesByName(m_eq("MyScriptTable"), any[GlobalSearchScope])).thenReturn(Array(myPsiClass))
  }

  def scriptRow(s: String): ScriptRow = {
    createTable("| script:MyScriptTable|\n" + s).rows(1).asInstanceOf[ScriptRow]
  }

  test("find table name") {
    val table = createTable("| script:MyScriptTable|")

    assertResult("MyScriptTable") {
      table.fixtureClass.get.fixtureClassName.get
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

    assertResult("foo" :: Nil) {
      output.parameters
    }

    assertResult(psiClassType("java.lang.String")) {
      output.returnType
    }

  }

  test("method name for symbol assignment") {
    val output = scriptRow("| $TR= | method | foo | invocation |")
    assertResult("methodInvocation") {
      output.fixtureMethodName
    }
  }

  def assertCommentRow(s: String): SimpleRow = {
    val psiFile = myPsiFileFactory.createFileFromText(FitnesseLanguage.INSTANCE,
      "| script|\n" + s)
    psiFile.getNode.getPsi(classOf[FitnesseFile]).getTables(0).rows(1).asInstanceOf[SimpleRow]
  }

  test("rows starting with note should be considered comments") {
    assertCommentRow("| note | this is a comment |")
  }

  test("rows starting with '*' should be considered comments") {
    assertCommentRow("| * | this is a comment |")
  }

  test("rows starting with '#' should be considered comments") {
    assertCommentRow("| # | this is a comment |")
  }

  test("rows starting with black first cell should be considered comments") {
    assertCommentRow("| | this is a comment |")
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
    assertCommentRow("| |")
  }

  test("scenario method") {
    val myScenarioCallMe = mock[ScenarioName]
    val output = scriptRow("| call me |")
    when(myPsiClass.findMethodsByName(m_eq("callMe"), anyBoolean)).thenReturn(Array[PsiMethod]())
    when(myStubIndex.get(m_eq(ScenarioNameIndex.KEY), m_eq("callMe"), any[Project], any[GlobalSearchScope])).thenReturn(List(myScenarioCallMe).asJava)
    assertResult(myScenarioCallMe) {
      val refs: Array[PsiReference] = output.getReferences
      refs(0).resolve
    }
  }

  test("parameters name for sequencial function call") {
    val output = scriptRow("| method | foo | invocation | bar baz |")
    assertResult("foo" :: "barBaz" :: Nil) {
      output.parameters
    }
  }

}
