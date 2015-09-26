package fitnesse.idea.decisiontable

import com.intellij.openapi.project.Project
import com.intellij.psi._
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs._
import fitnesse.idea.lang.parser.FitnesseElementType
import fitnesse.idea.lang.psi.{MockIndexSink, PsiSuite, Table}
import fitnesse.idea.scripttable._
import org.mockito.Matchers.{any, anyBoolean, eq => m_eq}
import org.mockito.Mockito.when

import scala.collection.JavaConverters._

class DecisionTableSuite extends PsiSuite {


  val myPsiClass = mock[PsiClass]
  val myPsiMethodSetA = mock[PsiMethod]
  val myPsiMethodC = mock[PsiMethod]
  val myPsiMethodFancyLongName = mock[PsiMethod]
  val myPsiMethodFancyQueryName = mock[PsiMethod]

  var table: Table = null
  
  override protected def beforeAll(): Unit = {
    super.beforeAll()

    when(myPsiShortNamesCache.getClassesByName(m_eq("DecisionTable"), any[GlobalSearchScope])).thenReturn(Array(myPsiClass))
    when(myPsiClass.findMethodsByName(m_eq("setA"), anyBoolean)).thenReturn(Array(myPsiMethodSetA))
    when(myPsiClass.findMethodsByName(m_eq("c"), anyBoolean)).thenReturn(Array(myPsiMethodC))
    when(myPsiClass.findMethodsByName(m_eq("setFancyLongName"), anyBoolean)).thenReturn(Array(myPsiMethodFancyLongName))
    when(myPsiClass.findMethodsByName(m_eq("fancyQueryName"), anyBoolean)).thenReturn(Array(myPsiMethodFancyQueryName))

    ElementManipulators.INSTANCE.addExplicitExtension(classOf[DecisionInput], new DecisionInputManipulator)

    table = createTable("| decision table |\n| a | b | c? | fancy long name | fancy query name? |\n| 1 | 2 | 3 |")
  }

  test("find table name") {
    assertResult("DecisionTable") {
      table.fixtureClass.get.fixtureClassName.get
    }
  }

  test("find decision table setter") {
    val input = table.rows(1).cells(0).asInstanceOf[DecisionInput]
    assertResult("setA") {
      input.fixtureMethodName
    }

    assertResult(myPsiMethodSetA) {
      val refs = input.getReferences
      refs(0).resolve
    }
  }

  test("find decision table query") {
    val output = table.rows(1).cells(2).asInstanceOf[DecisionOutput]
    assertResult("c") {
      output.fixtureMethodName
    }

    assertResult(myPsiMethodC) {
      val refs = output.getReferences
      refs(0).resolve
    }
  }

  test("handle graceful names") {
    val output = table.rows(1).cells(4).asInstanceOf[DecisionOutput]
    assertResult("fancyQueryName") {
      output.fixtureMethodName
    }

    assertResult(myPsiMethodFancyQueryName) {
      val refs = output.getReferences
      refs(0).resolve
    }
  }

  test("parameters for input fields") {
    val input = table.rows(1).cells(0).asInstanceOf[DecisionInput]
    assertResult("a" :: Nil) {
      input.parameters
    }
  }

  test("return for input fields") {
    val input = table.rows(1).cells(0).asInstanceOf[DecisionInput]
    assertResult(PsiType.VOID) {
      input.returnType
    }
  }

  test("parameters for output fields") {
    val output = table.rows(1).cells(4).asInstanceOf[DecisionOutput]
    assertResult(Nil) {
      output.parameters
    }
  }



  test("return for output fields") {
    val output = table.rows(1).cells(4).asInstanceOf[DecisionOutput]

    assertResult(psiClassType("java.lang.String")) {
      output.returnType
    }
  }

  test("scenario reference") {
    val myScenarioCallMe: ScenarioName = ScenarioNameImpl(new ScenarioNameStubImpl(mock[StubBase[Table]], "callMe", List("arg1", "arg2")))
    val output = createTable("| call me |").fixtureClass.get
    when(myStubIndex.get(m_eq(ScenarioNameIndex.KEY), m_eq("CallMe"), any[Project], any[GlobalSearchScope])).thenReturn(List(myScenarioCallMe).asJava)
    bypassShortNameCache()
    assertResult(myScenarioCallMe) {
      val refs = output.getReferences
      refs(0).resolve
    }
  }


  test("scenario arguments as completion options") {
    val myScenarioCallMe: ScenarioName = ScenarioNameElementType.INSTANCE.createPsi(new ScenarioNameStubImpl(mock[StubBase[Table]], "decision table", List("arg1", "arg2")))
    val decisionInput = table.rows(1).cells(0)
    when(myStubIndex.get(m_eq(ScenarioNameIndex.KEY), m_eq("DecisionTable"), any[Project], any[GlobalSearchScope])).thenReturn(List(myScenarioCallMe).asJava)
    bypassShortNameCache()
    assertResult(List("arg1", "arg2")) {
      val ref = decisionInput.getReference
      ref.getVariants
    }
  }

  test("can create stubs for decision table input") {
    val deserialized: Stub = createFileAndSerializeAndDeserialize("| decision table |\n| a |\n| 1 | 2 | 3 |")

    assertResult("[FixtureClassStubImpl, DecisionInputStubImpl]") {
      deserialized.getChildrenStubs.toString
    }

    val decisionInputStub = deserialized.getChildrenStubs.get(1).asInstanceOf[DecisionInputStub]
    val decisionInputPsi = FitnesseElementType.DECISION_INPUT.createPsi(decisionInputStub)

    assertResult("a") { decisionInputPsi.name }
    assertResult("a" :: Nil) { decisionInputPsi.parameters }
    assertResult(PsiType.VOID) { decisionInputPsi.returnType }
  }

  test("can create stubs for decision table output") {
    val deserialized: Stub = createFileAndSerializeAndDeserialize("| decision table |\n| foo bar? |\n| 1 | 2 | 3 |")

    assertResult("[FixtureClassStubImpl, DecisionOutputStubImpl]") {
      deserialized.getChildrenStubs.toString
    }

    val decisionOutputStub = deserialized.getChildrenStubs.get(1).asInstanceOf[DecisionOutputStub]
    val decisionOutputPsi = FitnesseElementType.DECISION_OUTPUT.createPsi(decisionOutputStub)

    assertResult("foo bar?") { decisionOutputPsi.name }
    assertResult(Nil) { decisionOutputPsi.parameters }
//    assertResult("java.lang.String") { decisionOutputPsi.returnType.toString } can only resolve with a Node
  }

  test("index input field") {
    val input = table.rows(1).cells(0).asInstanceOf[DecisionInput]
    val stub = DecisionInputElementType.INSTANCE.createStub(input, null)
    val indexSink = new MockIndexSink()
    DecisionInputElementType.INSTANCE.indexStub(stub, indexSink)
    assertResult("setA") {
      indexSink.value
    }
  }

  test("index output field") {
    val output = table.rows(1).cells(2).asInstanceOf[DecisionOutput]
    val stub = DecisionOutputElementType.INSTANCE.createStub(output, null)
    val indexSink = new MockIndexSink()
    DecisionOutputElementType.INSTANCE.indexStub(stub, indexSink)
    assertResult("c") {
      indexSink.value
    }
  }

  /* Call this in case the PsiShortNameCache is hit, but no results are expected. Defaults to empty array */
  def bypassShortNameCache(): Unit = {
    when(myPsiShortNamesCache.getClassesByName(any[String], any[GlobalSearchScope])).thenReturn(Array[PsiClass]())
  }
}
