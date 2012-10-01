package com.gshakhn.idea.idea.fitnesse.lang.reference

import com.intellij.psi.{PsiReferenceBase, PsiClass}
import com.gshakhn.idea.idea.fitnesse.lang.psi.FixtureClass

class FixtureClassReference(psiClass: PsiClass, element: FixtureClass) extends PsiReferenceBase[FixtureClass](element) {
  def resolve() = psiClass

  def getVariants = Array()
}
