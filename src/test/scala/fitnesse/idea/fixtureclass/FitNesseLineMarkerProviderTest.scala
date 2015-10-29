package fitnesse.idea.fixtureclass

import java.util

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.impl.light.JavaIdentifier
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.{PsiClass, PsiElement, PsiMethod}
import fitnesse.idea.psi.PsiSuite
import org.mockito.Matchers.{any, eq => m_eq}
import org.mockito.Mockito._

import scala.collection.JavaConverters._

class FitNesseLineMarkerProviderTest extends PsiSuite {

  test("navigation marker") {
    val markerProvider = new FitNesseLineMarkerProvider
    val clazz = mock[PsiClass]
    val method = mock[PsiMethod]
    when(method.getParent).thenReturn(clazz)
    when(method.getText).thenReturn("methodName")
    when(method.getTextRange).thenReturn(new TextRange(1, 3))
    val myFixtureClass = mock[FixtureClass]
    when(myStubIndex.get(m_eq(FixtureClassIndex.KEY), m_eq("methodName"), any[Project], any[GlobalSearchScope])).thenReturn(List(myFixtureClass).asJava)

    val element = new JavaIdentifier(myPsiManager, method)
    val result: util.List[_ >: RelatedItemLineMarkerInfo[_ <: PsiElement]] = new util.ArrayList[RelatedItemLineMarkerInfo[_ <: PsiElement]]()
    markerProvider.collectNavigationMarkers(element, result)

    assert(result.size() == 1)
  }
}
