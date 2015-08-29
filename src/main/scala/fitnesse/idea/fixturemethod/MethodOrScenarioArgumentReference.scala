package fitnesse.idea.fixturemethod

import com.intellij.psi._
import fitnesse.idea.lang.Regracer
import fitnesse.idea.scripttable.ScenarioName

class MethodOrScenarioArgumentReference(referer: FixtureMethod) extends MethodReference(referer) {

  override def getVariants = referer.getFixtureClass match {
    case Some(fixtureClass) =>
      fixtureClass.getReference.resolve match {
        case c: PsiClass => c.getAllMethods.map(m => Regracer.regrace(m.getName))
        case s: ScenarioName => s.getArguments.toArray
      }
    case None => Array.emptyObjectArray
  }
}
