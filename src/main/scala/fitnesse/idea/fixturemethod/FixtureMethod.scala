package fitnesse.idea.fixturemethod

import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}
import com.intellij.psi.{PsiClass, PsiElement, PsiMethod, PsiReference}
import fitnesse.idea.fixtureclass.FixtureClass
import fitnesse.idea.scripttable.ScenarioName

trait FixtureMethod extends PsiElement {

  def getFixtureClass: Option[FixtureClass]

  def fixtureMethodName: String

  override def getReference: MethodReference
}
