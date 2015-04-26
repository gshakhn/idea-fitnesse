package com.gshakhn.idea.idea.fitnesse.querytable

import com.intellij.psi.{PsiMethod, PsiReferenceBase}

class QueryOutputReference(psiMethod: PsiMethod, element: QueryOutput) extends PsiReferenceBase[QueryOutput](element) {
   override def resolve() = psiMethod

   override def getVariants = Array()
}
