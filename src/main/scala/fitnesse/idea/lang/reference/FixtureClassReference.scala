package fitnesse.idea.lang.reference

import com.intellij.psi.{PsiClass, PsiReferenceBase}
import fitnesse.idea.lang.psi.FixtureClass

class FixtureClassReference(psiClass: PsiClass, element: FixtureClass) extends PsiReferenceBase[FixtureClass](element) {
  override def resolve() = psiClass

  override def getVariants = Array()
}
