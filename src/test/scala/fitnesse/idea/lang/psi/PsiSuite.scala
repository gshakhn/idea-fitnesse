package fitnesse.idea.lang.psi

import com.intellij.psi.search.{ProjectScopeBuilder, ProjectScopeBuilderImpl, PsiShortNamesCache}
import com.intellij.psi.stubs.StubIndex
import fitnesse.idea.lang.parser.ParserSuite
import fitnesse.idea.scripttable.ScenarioNameIndex
import org.scalatest.mock.MockitoSugar

trait PsiSuite extends ParserSuite with MockitoSugar {

  var myPsiShortNamesCache: PsiShortNamesCache = mock[PsiShortNamesCache]
  var myStubIndex: StubIndex = mock[StubIndex]

  override protected def beforeAll(): Unit = {
    super.beforeAll()

    app.getPicoContainer.registerComponentInstance(classOf[StubIndex].getName, myStubIndex)
    myProject.getPicoContainer.registerComponentInstance(classOf[PsiShortNamesCache].getName, myPsiShortNamesCache)
    myProject.getPicoContainer.registerComponentInstance(classOf[ProjectScopeBuilder].getName, new ProjectScopeBuilderImpl(myProject))
  }

  override protected def afterAll(): Unit = {
    myProject.getPicoContainer.unregisterComponent(classOf[ProjectScopeBuilder].getName)
    myProject.getPicoContainer.unregisterComponent(classOf[PsiShortNamesCache].getName)
    app.getPicoContainer.unregisterComponent(classOf[StubIndex].getName)

    super.afterAll()
  }
}