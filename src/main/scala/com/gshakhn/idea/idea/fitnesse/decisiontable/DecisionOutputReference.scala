package com.gshakhn.idea.idea.fitnesse.decisiontable

import com.intellij.psi.{PsiMethod, PsiReferenceBase}

class DecisionOutputReference(psiMethod: PsiMethod, element: DecisionOutput) extends PsiReferenceBase[DecisionOutput](element) {
   override def resolve() = psiMethod

   override def getVariants = Array()
}
