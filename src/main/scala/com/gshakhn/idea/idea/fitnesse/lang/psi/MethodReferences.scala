package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}
import com.intellij.psi.{PsiReference, PsiMethod}

trait MethodReferences { self: Cell =>

  def fixtureClassName = getRow.getTable.getFixtureClass.fixtureClassName

  def fixtureMethodName: String

  def createReference(psiMethod: PsiMethod): PsiReference

  override def getReferences: Array[PsiReference] = {
    // PsiShortNamesCache: Allows to retrieve files and Java classes, methods and fields in a project by non-qualified names
    val cache = PsiShortNamesCache.getInstance(getProject)
    val classes = cache.getClassesByName(fixtureClassName, GlobalSearchScope.projectScope(getProject))

    classes.flatMap(_.findMethodsByName(fixtureMethodName, true /* checkBases */))
      .map(createReference)
  }

}
