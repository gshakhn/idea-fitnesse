package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}
import com.intellij.psi.{PsiMethod, PsiReference}

trait MethodReferences { self: ASTWrapperPsiElement =>

  def fixtureClassName: Option[String]

  def fixtureMethodName: String

  def createReference(psiMethod: PsiMethod): MethodReference = new MethodReference(psiMethod, this)

  override def getReferences: Array[PsiReference] = {
    fixtureClassName match {
      case Some(name) => {
        // PsiShortNamesCache: Allows to retrieve files and Java classes, methods and fields in a project by non-qualified names
        val cache = PsiShortNamesCache.getInstance(getProject)
        val classes = cache.getClassesByName(name, GlobalSearchScope.projectScope(getProject))

        classes.flatMap(_.findMethodsByName(fixtureMethodName, true /* checkBases */))
          .map(createReference)
      }
      case None => {
        val cache = PsiShortNamesCache.getInstance(getProject)
        val methods = cache.getMethodsByName(fixtureMethodName, GlobalSearchScope.projectScope(getProject))

        methods.map(createReference)
      }
    }
  }

}
