package fitnesse.idea.fixtureclass

import com.intellij.psi.PsiClass
import com.intellij.psi.search.GlobalSearchScope
import fitnesse.idea.lang.psi.{FitnesseElementFactory, FitnesseFile, PsiSuite, Table}
import org.mockito.Matchers.{any, eq => m_eq}
import org.mockito.Mockito._

class FixtureClassReferenceTest extends PsiSuite {

  def createTable(s: String): Table = {
    val psiFile = FitnesseElementFactory.createFile(myProject, s)
    psiFile.getNode.getPsi(classOf[FitnesseFile]).getTables(0)
  }

  test("resolve simple class name") {
    val table = createTable("| script | table name |")
    val myPsiClass = mock[PsiClass]
    when(myPsiShortNamesCache.getClassesByName(m_eq("TableName"), any[GlobalSearchScope])).thenReturn(Array(myPsiClass))

    val result = table.getFixtureClass.get.getReference.multiResolve(false)

    assertResult(1) { result.length }
    assertResult(myPsiClass) { result(0).getElement }
  }

  // TODO: resolve fully qualified class name

  // TODO: resolve class + scenario for decision table

  // TODO: variants
}
