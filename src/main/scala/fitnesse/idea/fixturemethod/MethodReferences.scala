package fitnesse.idea.fixturemethod

import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}
import com.intellij.psi.{PsiClass, PsiElement, PsiMethod, PsiReference}
import fitnesse.idea.fixtureclass.FixtureClass

trait MethodReferences extends PsiElement {

  def getFixtureClass: Option[FixtureClass]

  def fixtureMethodName: String

  protected def createReference(psiMethod: PsiMethod): MethodReference = new MethodReference(psiMethod, this)

  protected def getReferencedMethods: Seq[PsiReference] = {
    getFixtureClass match {
      case Some(fixtureClass) =>
        fixtureClass.getReferences
          .flatMap(_.resolve.asInstanceOf[PsiClass].findMethodsByName(fixtureMethodName, true /* checkBases */)).map(createReference)
      case None =>
        val cache = PsiShortNamesCache.getInstance(getProject)
        cache.getMethodsByName(fixtureMethodName, GlobalSearchScope.projectScope(getProject)).map(createReference)
    }
  }

  override def getReferences: Array[PsiReference] = getReferencedMethods.toArray
}
