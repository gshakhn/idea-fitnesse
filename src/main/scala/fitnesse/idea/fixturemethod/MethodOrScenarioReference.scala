package fitnesse.idea.fixturemethod

import com.intellij.psi.ResolveResult
import com.intellij.psi.search.GlobalSearchScope
import fitnesse.idea.lang.Regracer
import fitnesse.idea.scripttable.ScenarioNameIndex

import scala.collection.JavaConversions._

class MethodOrScenarioReference(referer: FixtureMethod) extends MethodReference(referer) {

  override def getVariants = {
    val scenarioNames: Array[Object] =  ScenarioNameIndex.INSTANCE.getAllKeys(project).map(Regracer.regrace).toArray
    Array.concat(super.getVariants, scenarioNames)
  }

  override def multiResolve(b: Boolean): Array[ResolveResult] = (getReferencedScenarios ++ getReferencedMethods).toArray

  protected def getReferencedScenarios: Seq[ResolveResult] = {
    ScenarioNameIndex.INSTANCE.get(referer.fixtureMethodName, project, GlobalSearchScope.projectScope(project)).map(createReference).toSeq
  }
}
