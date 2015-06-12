package fitnesse.idea.fixturemethod

import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}
import com.intellij.psi.{PsiElement, PsiMethod, PsiReference}
import fitnesse.idea.fixtureclass.FixtureClass

trait MethodReferences extends PsiElement {

  def getFixtureClass: Option[FixtureClass]

  def fixtureMethodName: String

  def getReferencedMethods: Seq[PsiMethod] = {
    getFixtureClass match {
      case Some(fixtureClass) =>
        fixtureClass.getReferencedClasses
          .flatMap(_.findMethodsByName(fixtureMethodName, true /* checkBases */)).toSeq
      case None =>
        val cache = PsiShortNamesCache.getInstance(getProject)
        cache.getMethodsByName(fixtureMethodName, GlobalSearchScope.projectScope(getProject))
    }
  }


  override def getReferences: Array[PsiReference] = {
    getReferencedMethods.map(createReference).toArray
  }

  private def createReference(psiMethod: PsiMethod): MethodReference = new MethodReference(psiMethod, this)
}
