package fitnesse.idea.fixturemethod

import com.intellij.psi.ResolveResult
import fitnesse.idea.etc.Regracer
import fitnesse.idea.etc.SearchScope.searchScope
import fitnesse.idea.scenariotable.ScenarioNameIndex

import scala.collection.JavaConversions._

class MethodOrScenarioReference(referer: FixtureMethod) extends MethodReference(referer) {

  override def getVariants = {
    val scenarioNames: Array[Object] =  ScenarioNameIndex.INSTANCE.getAllKeys(project).map(Regracer.regrace).toArray
    Array.concat(super.getVariants, scenarioNames)
  }

  override def multiResolve(b: Boolean): Array[ResolveResult] = (getReferencedScenarios ++ getReferencedMethods).toArray

//// Change to this implementation, once we handle only the applicable ScenarioLibrary pages:
//  override def multiResolve(b: Boolean): Array[ResolveResult] = {
//    getReferencedScenarios match {
//      case Nil => getReferencedMethods.toArray
//      case referencedScenarios => referencedScenarios.toArray
//    }
//  }

  protected def getReferencedScenarios: Seq[ResolveResult] = {
    ScenarioNameIndex.INSTANCE.get(referer.fixtureMethodName, project, searchScope(project)).map(createReference).toSeq
  }
}
