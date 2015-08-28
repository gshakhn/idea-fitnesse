package fitnesse.idea.fixturemethod

import com.intellij.psi._
import com.intellij.psi.search.GlobalSearchScope
import fitnesse.idea.lang.Regracer
import fitnesse.idea.scripttable.{ScenarioName, ScenarioNameIndex}

import scala.collection.JavaConversions._

// Reference needs
class MethodOrScenarioArgumentReference(referer: FixtureMethod) extends MethodReference(referer) {

  override def getVariants = referer.getFixtureClass match {
    case Some(fixtureClass) =>
      fixtureClass.getReference.resolve match {
        case c: PsiClass => c.getAllMethods.map(m => Regracer.regrace(m.getName))
        case s: ScenarioName => s.getArguments.toArray
      }
    case None => Array.emptyObjectArray
  }

  override def multiResolve(b: Boolean): Array[ResolveResult] = (getReferencedScenarios ++ getReferencedMethods).toArray

  protected def getReferencedScenarios: Seq[ResolveResult] = referer.getFixtureClass match {
    case Some(fixtureClass) => fixtureClass.fixtureClassName match {
      case Some(className) =>
        ScenarioNameIndex.INSTANCE.get(className, project, GlobalSearchScope.projectScope(project)).map(createReference).toSeq
      case None => List()
    }
    case None => List()
  }
}
