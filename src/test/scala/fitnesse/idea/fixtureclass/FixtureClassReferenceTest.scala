package fitnesse.idea.fixtureclass

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StubBase
import fitnesse.idea.psi.PsiSuite
import fitnesse.idea.scenariotable.{ScenarioNameElementType, ScenarioNameIndex, ScenarioNameStubImpl, ScenarioName}
import fitnesse.idea.scripttable._
import fitnesse.idea.table.Table
import org.mockito.Matchers.{any, eq => m_eq}
import org.mockito.Mockito._

import scala.collection.JavaConverters._

class FixtureClassReferenceTest extends PsiSuite {

  test("resolve simple class name") {
    val table = createTable("| script | table name |")
    val myPsiClass = mock[PsiClass]
    when(myPsiShortNamesCache.getClassesByName(m_eq("TableName"), any[GlobalSearchScope])).thenReturn(Array(myPsiClass))

    val result = table.fixtureClass.get.getReference.multiResolve(false)

    assertResult(1) { result.length }
    assertResult(myPsiClass) { result(0).getElement }
  }

  test("resolve fully qualified class name") {
    val table = createTable("| script | eg.SampleTable |")
    val myPsiClass = mock[PsiClass]
    when(myJavaPsiFacade.findClasses(m_eq("eg.SampleTable"), any[GlobalSearchScope])).thenReturn(Array(myPsiClass))

    val result = table.fixtureClass.get.getReference.multiResolve(false)

    assertResult(1) { result.length }
    assertResult(myPsiClass) { result(0).getElement }
  }

  test("resolve class + scenario for decision table") {
    val table = createTable("| script | table name |")
    val myPsiClass = mock[PsiClass]
    val myScenario: ScenarioName = ScenarioNameElementType.INSTANCE.createPsi(new ScenarioNameStubImpl(mock[StubBase[Table]], "decision table", List()))
    when(myPsiShortNamesCache.getClassesByName(m_eq("TableName"), any[GlobalSearchScope])).thenReturn(Array(myPsiClass))
    when(myStubIndex.get(m_eq(ScenarioNameIndex.KEY), m_eq("TableName"), any[Project], any[GlobalSearchScope])).thenReturn(List(myScenario).asJava)
    val result = table.fixtureClass.get.getReference.multiResolve(false)

    assertResult(1) { result.length }
    assertResult(myPsiClass) { result(0).getElement }
  }

  test("completion options for a fixture class") {
    val table = createTable("| script | table name |")
    when(myPsiShortNamesCache.getAllClassNames()).thenReturn(Array("FixtureClass"))
    when(myStubIndex.getAllKeys(m_eq(ScenarioNameIndex.KEY), any[Project])).thenReturn(List("Scenario").asJava)

    val result = table.fixtureClass.get.getReference.getVariants()

    assertResult(1) { result.length }
    assertResult("fixture class") { result(0) }
  }

  test("completion options for decision table should contain both fixture classes and scenarios") {
    val table = createTable("| table name |")
    when(myPsiShortNamesCache.getAllClassNames()).thenReturn(Array("FixtureClass"))
    when(myStubIndex.getAllKeys(m_eq(ScenarioNameIndex.KEY), any[Project])).thenReturn(List("Scenario").asJava)

    val result = table.fixtureClass.get.getReference.getVariants()

    assertResult(2) { result.length }
    assertResult("fixture class") { result(0) }
    assertResult("Scenario") { result(1) }
  }

}
