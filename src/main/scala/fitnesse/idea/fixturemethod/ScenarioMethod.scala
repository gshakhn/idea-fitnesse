package fitnesse.idea.fixturemethod

import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.{PsiElement, PsiReference}
import fitnesse.idea.scripttable.{ScenarioName, ScenarioNameIndex}

import scala.collection.JavaConversions._

trait ScenarioMethod extends PsiElement {

  def fixtureMethodName: String

  def getReferencedScenarios: Seq[PsiReference] = {
    ScenarioNameIndex.INSTANCE.get(fixtureMethodName, getProject, GlobalSearchScope.projectScope(getProject)).map(new ScenarioReference(_, this)).toSeq
  }
}