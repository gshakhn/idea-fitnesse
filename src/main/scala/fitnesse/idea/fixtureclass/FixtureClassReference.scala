package fitnesse.idea.fixtureclass

import com.intellij.psi.{PsiClass, PsiReferenceBase}

class FixtureClassReference(psiClass: PsiClass, element: FixtureClass) extends PsiReferenceBase[FixtureClass](element) {
  override def resolve() = psiClass

  override def getVariants = Array()
}
