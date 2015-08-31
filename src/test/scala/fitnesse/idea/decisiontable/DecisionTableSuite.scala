package fitnesse.idea.decisiontable

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StubBase
import com.intellij.psi.{ElementManipulators, PsiClass, PsiMethod}
import fitnesse.idea.lang.psi.{FitnesseElementFactory, FitnesseFile, PsiSuite, Table}
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

    table = decisionTable("| decision table |\n| a | b | c? | fancy long name | fancy query name? |\n| 1 | 2 | 3 |")
  }

  def decisionTable(s: String): Table = {
    val psiFile = FitnesseElementFactory.createFile(myProject, s)
    psiFile.getNode.getPsi(classOf[FitnesseFile]).getTables(0)
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

  test("scenario reference") {
    val myScenarioCallMe: ScenarioName = ScenarioNameImpl(new ScenarioNameStubImpl(mock[StubBase[Table]], "callMe", List("arg1", "arg2")))
    val output = decisionTable("| call me |").getFixtureClass.get
    when(myStubIndex.get(m_eq(ScenarioNameIndex.KEY), m_eq("CallMe"), any[Project], any[GlobalSearchScope])).thenReturn(List(myScenarioCallMe).asJava)
    bypassShortNameCache()
    assertResult(myScenarioCallMe) {
      val refs = output.getReferences
      refs(0).resolve
    }
  }


  test("scenario arguments as completion options") {
    val myScenarioCallMe: ScenarioName = ScenarioNameElementType.INSTANCE.createPsi(new ScenarioNameStubImpl(mock[StubBase[Table]], "decision table", List("arg1", "arg2")))
    val decisionInput = table.getRows(1).getCells(0)
    when(myStubIndex.get(m_eq(ScenarioNameIndex.KEY), m_eq("DecisionTable"), any[Project], any[GlobalSearchScope])).thenReturn(List(myScenarioCallMe).asJava)
    bypassShortNameCache()
    assertResult(List("arg1", "arg2")) {
      val ref = decisionInput.getReference
      ref.getVariants
    }
  }

  /* Call this in case the PsiShortNameCache is hit, but no results are expected. Defaults to empty array */
  def bypassShortNameCache(): Unit = {
    when(myPsiShortNamesCache.getClassesByName(any[String], any[GlobalSearchScope])).thenReturn(Array[PsiClass]())
  }
}
