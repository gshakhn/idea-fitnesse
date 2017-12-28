package fitnesse.idea.scenariotable

import com.intellij.psi.stubs.Stub
import fitnesse.idea.parser.FitnesseElementType
import fitnesse.idea.psi.{MockIndexSink, PsiSuite}
import fitnesse.idea.table.Table
import org.mockito.Matchers.{eq => m_eq}

class ScenarioTableSuite extends PsiSuite {

  def scenarioName(table: Table): ScenarioName = table.findInFirstRow(classOf[ScenarioName]).get

  test("find table name") {
    val table = createTable("| scenario | my scenario table |")

    assertResult("my scenario table") {
      scenarioName(table).name
    }

    assertResult("MyScenarioTable") {
      scenarioName(table).scenarioName
    }
  }

  test("find table name for table template") {
    val table = createTable("| table template | my scenario table |")

    assertResult("my scenario table") {
      scenarioName(table).name
    }

    assertResult("MyScenarioTable") {
      scenarioName(table).scenarioName
    }
  }

  test("find interposed table name") {
    val table = createTable("| scenario | my | val1 |scenario | val2 | table |")

    assertResult("my scenario table") {
      scenarioName(table).name
    }

    assertResult("MyScenarioTable") {
      scenarioName(table).scenarioName
    }
  }

  test("find interposed table arguments") {
    val table = createTable("| scenario | my | val1 |scenario | val2 | table |")

    assertResult(List("val1", "val2")) {
      scenarioName(table).arguments
    }
  }

  test("find parameterized table name") {
    val table = createTable("| scenario | my _ scenario _ table | val1, val2 |")

    assertResult("my scenario table") {
      scenarioName(table).name
    }

    assertResult("MyScenarioTable") {
      scenarioName(table).scenarioName
    }
  }

  test("find parameterized table arguments") {
    val table = createTable("| scenario | my _ scenario _ table | val1, val2 |")

    assertResult(List("val1", "val2")) {
      scenarioName(table).arguments
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
    val name = scenarioName(table)
    val stub = ScenarioNameElementType.INSTANCE.createStub(name, null)
    val indexSink = new MockIndexSink()
    ScenarioNameElementType.INSTANCE.indexStub(stub, indexSink)
    assertResult("myScenarioTable") {
      indexSink.value
    }
  }
}
