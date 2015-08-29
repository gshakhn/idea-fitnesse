package fitnesse.idea.fixturemethod

import fitnesse.idea.lang.Regracer
import fitnesse.idea.scripttable.ScenarioNameIndex

import scala.collection.JavaConversions._

class MethodOrScenarioReference(referer: FixtureMethod) extends MethodReference(referer) {

  override def getVariants = {
    val scenarioNames: Array[Object] =  ScenarioNameIndex.INSTANCE.getAllKeys(project).map(Regracer.regrace).toArray
    Array.concat(super.getVariants, scenarioNames)
  }
}
