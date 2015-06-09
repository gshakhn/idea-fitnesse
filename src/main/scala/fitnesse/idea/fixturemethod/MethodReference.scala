package fitnesse.idea.fixturemethod

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.psi.{PsiElement, PsiMethod, PsiReferenceBase}

class MethodReference(psiMethod: PsiMethod, element: MethodReferences) extends PsiReferenceBase[MethodReferences](element) {
   override def resolve() = psiMethod

   override def getVariants = Array()
}
