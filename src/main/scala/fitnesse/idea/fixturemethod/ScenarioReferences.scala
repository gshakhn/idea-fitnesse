package fitnesse.idea.fixturemethod

import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import fitnesse.idea.scripttable.{ScenarioNameIndex, ScenarioName}

trait ScenarioReferences extends PsiElement {

  def fixtureMethodName: String

  def getReferencedScenarios: Seq[ScenarioName] =
    ScenarioNameIndex.INSTANCE.get(fixtureMethodName, getProject, GlobalSearchScope.projectScope(getProject)).toSeq

}