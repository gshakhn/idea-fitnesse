package fitnesse.idea.scripttable

import com.intellij.psi.stubs.Stub
import fitnesse.idea.lang.parser.FitnesseElementType
import fitnesse.idea.lang.psi.{MockIndexSink, PsiSuite}
import org.mockito.Matchers.{eq => m_eq}

class ScenarioTableSuite extends PsiSuite {

  test("find table name") {
    val table = createTable("| scenario | my scenario table |")

    assertResult("my scenario table") {
      table.scenarioName.get.name
    }

    assertResult("MyScenarioTable") {
      table.scenarioName.get.scenarioName
    }
  }

  test("find interposed table name") {
    val table = createTable("| scenario | my | val1 |scenario | val2 | table |")

    assertResult("my scenario table") {
      table.scenarioName.get.name
    }

    assertResult("MyScenarioTable") {
      table.scenarioName.get.scenarioName
    }
  }

  test("find interposed table arguments") {
    val table = createTable("| scenario | my | val1 |scenario | val2 | table |")

    assertResult(List("val1", "val2")) {
      table.scenarioName.get.arguments
    }
  }

  test("find parameterized table name") {
    val table = createTable("| scenario | my _ scenario _ table | val1, val2 |")

    assertResult("my scenario table") {
      table.scenarioName.get.name
    }

    assertResult("MyScenarioTable") {
      table.scenarioName.get.scenarioName
    }
  }

  test("find parameterized table arguments") {
    val table = createTable("| scenario | my _ scenario _ table | val1, val2 |")

    assertResult(List("val1", "val2")) {
      table.scenarioName.get.arguments
    }
  }

  test("can create stubs for scenario name") {
    val deserialized: Stub = createFileAndSerializeAndDeserialize("| scenario | my | val1 |scenario | val2 | table |")

    assertResult("[ScenarioNameStubImpl]") {
      deserialized.getChildrenStubs.toString
    }

    val scenarioNameStub = deserialized.getChildrenStubs.get(0).asInstanceOf[ScenarioNameStub]
    val scenarioNamePsi = FitnesseElementType.SCENARIO_NAME.createPsi(scenarioNameStub)

    assertResult("my scenario table") { scenarioNamePsi.name }
    assertResult(List("val1", "val2")) { scenarioNamePsi.arguments }
  }

  test("index scenario name") {
    val table = createTable("| scenario | my | val1 |scenario | val2 | table |")
    val scenarioName = table.scenarioName.get
    val stub = ScenarioNameElementType.INSTANCE.createStub(scenarioName, null)
    val indexSink = new MockIndexSink()
    ScenarioNameElementType.INSTANCE.indexStub(stub, indexSink)
    assertResult("myScenarioTable") {
      indexSink.value
    }
  }
}
