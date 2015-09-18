package fitnesse.idea.scripttable

import fitnesse.idea.lang.FitnesseLanguage
import fitnesse.idea.lang.psi.{FitnesseFile, PsiSuite}
import org.mockito.Matchers.{eq => m_eq}

class ScenarioTableSuite extends PsiSuite {

  test("find table name") {
    val psiFile = myPsiFileFactory.createFileFromText(FitnesseLanguage.INSTANCE,
      "| scenario | my scenario table |")
    val table = psiFile.getNode.getPsi(classOf[FitnesseFile]).getTables(0)

    assertResult("my scenario table") {
      table.scenarioName.get.name
    }

    assertResult("MyScenarioTable") {
      table.scenarioName.get.scenarioName
    }
  }

  test("find interposed table name") {
    val psiFile = myPsiFileFactory.createFileFromText(FitnesseLanguage.INSTANCE,
      "| scenario | my | val1 |scenario | val2 | table |")
    val table = psiFile.getNode.getPsi(classOf[FitnesseFile]).getTables(0)

    assertResult("my scenario table") {
      table.scenarioName.get.name
    }

    assertResult("MyScenarioTable") {
      table.scenarioName.get.scenarioName
    }
  }

  test("find parameterized table name") {
    val psiFile = myPsiFileFactory.createFileFromText(FitnesseLanguage.INSTANCE,
      "| scenario | my _ scenario _ table | val1, val2 |")
    val table = psiFile.getNode.getPsi(classOf[FitnesseFile]).getTables(0)

    assertResult("my scenario table") {
      table.scenarioName.get.name
    }

    assertResult("MyScenarioTable") {
      table.scenarioName.get.scenarioName
    }
  }

}
