package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.psi.{PsiMethod, PsiReferenceBase}

class MethodReference(psiMethod: PsiMethod, element: ASTWrapperPsiElement) extends PsiReferenceBase[ASTWrapperPsiElement](element) {
   override def resolve() = psiMethod

   override def getVariants = Array()
}
