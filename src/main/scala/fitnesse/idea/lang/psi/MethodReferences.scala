package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}
import com.intellij.psi.{PsiMethod, PsiReference}

trait MethodReferences { self: ASTWrapperPsiElement =>

  def getFixtureClass: Option[FixtureClass]

  def fixtureMethodName: String

  def createReference(psiMethod: PsiMethod): MethodReference = new MethodReference(psiMethod, this)

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
}
