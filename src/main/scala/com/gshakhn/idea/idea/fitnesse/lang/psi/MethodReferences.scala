package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}
import com.intellij.psi.{PsiMethod, PsiReference}

trait MethodReferences { self: ASTWrapperPsiElement =>

  def getFixtureClass: Option[FixtureClass]

  def fixtureMethodName: String

  def createReference(psiMethod: PsiMethod): MethodReference = new MethodReference(psiMethod, this)

  override def getReferences: Array[PsiReference] = {
    getFixtureClass match {
      case Some(fixtureClass) =>
        fixtureClass.getReferencedClasses
          .flatMap(_.findMethodsByName(fixtureMethodName, true /* checkBases */))
          .map(createReference)
      case None =>
        val cache = PsiShortNamesCache.getInstance(getProject)
        val methods = cache.getMethodsByName(fixtureMethodName, GlobalSearchScope.projectScope(getProject))

        methods.map(createReference)
    }
  }
}
