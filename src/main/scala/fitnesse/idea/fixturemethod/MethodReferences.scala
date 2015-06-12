package fitnesse.idea.fixturemethod

import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}
import com.intellij.psi.{PsiElement, PsiMethod, PsiReference}
import fitnesse.idea.fixtureclass.FixtureClass

trait MethodReferences extends PsiElement {

  def getFixtureClass: Option[FixtureClass]

  def fixtureMethodName: String

  protected def getReferencedMethods: Seq[PsiReference] = {
    def createReference(psiMethod: PsiMethod): MethodReference = new MethodReference(psiMethod, this)

    getFixtureClass match {
      case Some(fixtureClass) =>
        fixtureClass.getReferencedClasses
          .flatMap(_.findMethodsByName(fixtureMethodName, true /* checkBases */)).map(createReference)
      case None =>
        val cache = PsiShortNamesCache.getInstance(getProject)
        cache.getMethodsByName(fixtureMethodName, GlobalSearchScope.projectScope(getProject)).map(createReference)
    }
  }

  override def getReferences: Array[PsiReference] = getReferencedMethods.toArray
}
