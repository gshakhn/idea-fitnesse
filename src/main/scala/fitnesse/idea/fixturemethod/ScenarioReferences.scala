package fitnesse.idea.fixturemethod

import com.intellij.psi.{PsiMethod, PsiReference, PsiElement}
import com.intellij.psi.search.GlobalSearchScope
import fitnesse.idea.scripttable.{ScenarioNameIndex, ScenarioName}

import scala.collection.JavaConversions._

trait ScenarioReferences extends PsiElement {

  def fixtureMethodName: String

  def getReferencedScenarios: Seq[PsiReference] = {
    def createReference(scenarioName: ScenarioName): ScenarioReference = new ScenarioReference(scenarioName, this)
    ScenarioNameIndex.INSTANCE.get(fixtureMethodName, getProject, GlobalSearchScope.projectScope(getProject)).map(createReference).toSeq
  }
}