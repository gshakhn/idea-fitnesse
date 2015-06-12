package fitnesse.idea.fixturemethod

import com.intellij.psi.{PsiMethod, PsiReferenceBase}

class MethodReference(psiMethod: PsiMethod, element: MethodReferences) extends PsiReferenceBase[MethodReferences](element) {
   override def resolve() = psiMethod

   override def getVariants = Array()
}
