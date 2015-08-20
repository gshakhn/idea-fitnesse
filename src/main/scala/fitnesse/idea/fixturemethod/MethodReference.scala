package fitnesse.idea.fixturemethod

import com.intellij.psi.{PsiMethod, PsiReferenceBase}

class MethodReference(psiMethod: PsiMethod, element: FixtureMethod) extends PsiReferenceBase[FixtureMethod](element) {
   override def resolve = psiMethod

   override def getVariants = Array()
}
