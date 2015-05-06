package fitnesse.idea.lang.psi

import com.intellij.psi.search.{ProjectScopeBuilder, ProjectScopeBuilderImpl, PsiShortNamesCache}
import fitnesse.idea.lang.parser.ParserSuite
import org.scalatest.mock.MockitoSugar

trait PsiSuite extends ParserSuite with MockitoSugar {

  var myPsiShortNamesCache: PsiShortNamesCache = mock[PsiShortNamesCache]

  override protected def beforeAll() {
    super.beforeAll()

    myProject.getPicoContainer.registerComponentInstance(classOf[PsiShortNamesCache].getName, myPsiShortNamesCache)
    myProject.getPicoContainer.registerComponentInstance(classOf[ProjectScopeBuilder].getName, new ProjectScopeBuilderImpl(myProject))
  }

  override protected def afterAll() {
    myProject.getPicoContainer.unregisterComponent(classOf[PsiShortNamesCache].getName)
    myProject.getPicoContainer.unregisterComponent(classOf[ProjectScopeBuilder].getName)

    super.afterAll()
  }
}