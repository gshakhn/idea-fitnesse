package fitnesse.idea.fixtureclass

import com.intellij.psi._
import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}
import fitnesse.idea.decisiontable.DecisionTable
import fitnesse.idea.scripttable.ScenarioNameIndex

import scala.collection.JavaConversions._

class FixtureClassReference(referer: FixtureClassImpl) extends PsiPolyVariantReferenceBase[FixtureClass](referer) {

  val project = referer.getProject

  override def getVariants = {
    // TODO: If decision table: include scenario's and java classes
    // Return array of String, {@link PsiElement} and/or {@link LookupElement}
    Array("foo bar", "late at night")
  }

  override def multiResolve(b: Boolean): Array[ResolveResult] = referer.getTable match {
    case _: DecisionTable =>
      (getReferencedScenarios ++ getReferencedClasses).toArray
    case _ =>
      getReferencedClasses.toArray
  }


  protected def fixtureClassName = referer.fixtureClassName

  protected def isQualifiedName: Boolean = {
    fixtureClassName match {
      case Some(name) =>
        val dotIndex: Int = name.indexOf(".")
        dotIndex != -1 && dotIndex != name.length - 1
      case None => false
    }
  }

  protected def shortName: Option[String] = {
    fixtureClassName match {
      case Some(name) => name.split('.').toList.reverse match {
        case "" :: n :: _ => Some(n)
        case n :: _ => Some(n)
        case _ => Some(name)
      }
      case None => None
    }
  }

  private def createReference(element: PsiElement): ResolveResult = new PsiElementResolveResult(element)

  protected def getReferencedClasses: Seq[ResolveResult] = {
    fixtureClassName match {
      case Some(className) if isQualifiedName =>
        JavaPsiFacade.getInstance(project).findClasses(className, GlobalSearchScope.projectScope(project)).map(createReference)
      case Some(className) =>
        PsiShortNamesCache.getInstance(project).getClassesByName(shortName.get, GlobalSearchScope.projectScope(project)).map(createReference)
      case None => Seq()
    }
  }

  protected def getReferencedScenarios: Seq[ResolveResult] = {

    referer.fixtureClassName match {
      case Some(className) if isQualifiedName => Seq()
      case Some(className) =>
        ScenarioNameIndex.INSTANCE.get(className, project, GlobalSearchScope.projectScope(project)).map(createReference).toSeq
      case None => Seq()
    }
  }
}
