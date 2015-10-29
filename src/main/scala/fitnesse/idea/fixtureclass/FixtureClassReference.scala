package fitnesse.idea.fixtureclass

import com.intellij.openapi.module.{Module, ModuleUtilCore}
import com.intellij.openapi.project.Project
import com.intellij.psi._
import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}
import fitnesse.idea.decisiontable.DecisionTable
import fitnesse.idea.etc.Regracer
import fitnesse.idea.scenariotable.ScenarioNameIndex

import scala.collection.JavaConversions._

class FixtureClassReference(referer: FixtureClassImpl) extends PsiPolyVariantReferenceBase[FixtureClass](referer) {

  val project = referer.getProject
  def module = ModuleUtilCore.findModuleForPsiElement(referer)

  // Return array of String, {@link PsiElement} and/or {@link LookupElement}
  override def getVariants = {
    val allClassNames: Array[String] = PsiShortNamesCache.getInstance(project).getAllClassNames.filter(p => p != null).map(Regracer.regrace)
    referer.table match {
      case _ : DecisionTable =>
        val scenarioNames =  ScenarioNameIndex.INSTANCE.getAllKeys(project).map(Regracer.regrace).toArray
        Array.concat(allClassNames, scenarioNames).asInstanceOf[Array[AnyRef]]
      case _ =>
       allClassNames.asInstanceOf[Array[AnyRef]]
    }
  }

  override def multiResolve(b: Boolean): Array[ResolveResult] = referer.table match {
    case _: DecisionTable =>
      (getReferencedScenarios ++ getReferencedClasses).toArray
    case _ =>
      getReferencedClasses.toArray
  }

  private def fixtureClassName = referer.fixtureClassName

  protected def isQualifiedName: Boolean = fixtureClassName match {
    case Some(name) =>
      val dotIndex: Int = name.indexOf(".")
      dotIndex != -1 && dotIndex != name.length - 1
    case None => false
  }

  protected def shortName: Option[String] = fixtureClassName match {
    case Some(name) => name.split('.').toList.reverse match {
      case "" :: n :: _ => Some(n)
      case n :: _ => Some(n)
      case _ => Some(name)
    }
    case None => None
  }

  private def createReference(element: PsiElement): ResolveResult = new PsiElementResolveResult(element)

  protected def getReferencedClasses: Seq[ResolveResult] = fixtureClassName match {
    case Some(className) if isQualifiedName =>
//      JavaPsiFacade.getInstance(project).findClasses(className, GlobalSearchScope.projectScope(project)).map(createReference)
      JavaPsiFacade.getInstance(project).findClasses(className, FixtureClassReference.moduleWithDependenciesScope(module)).map(createReference)
    case Some(className) =>
      PsiShortNamesCache.getInstance(project).getClassesByName(shortName.get, FixtureClassReference.moduleWithDependenciesScope(module)).map(createReference)
    case None => Seq()
  }

  protected def getReferencedScenarios: Seq[ResolveResult] = referer.fixtureClassName match {
    case Some(className) if isQualifiedName => Seq()
    case Some(className) =>
      ScenarioNameIndex.INSTANCE.get(className, project, FixtureClassReference.projectScope(project)).map(createReference).toSeq
    case None => Seq()
  }

}

// This is a work-around for testing:

object FixtureClassReference {
  /**
   * Override `scopeForTesting` for testing.
   */
  var scopeForTesting: Option[GlobalSearchScope] = None

  def moduleWithDependenciesScope(module: Module): GlobalSearchScope = scopeForTesting match {
    case Some(scope) => scope
    case None => GlobalSearchScope.moduleWithDependenciesScope(module)
  }

  def moduleScope(module: Module): GlobalSearchScope = scopeForTesting match {
    case Some(scope) => scope
    case None => GlobalSearchScope.moduleScope(module)
  }

  def projectScope(project: Project): GlobalSearchScope = scopeForTesting match {
    case Some(scope) => scope
    case None => GlobalSearchScope.projectScope(project)
  }
}