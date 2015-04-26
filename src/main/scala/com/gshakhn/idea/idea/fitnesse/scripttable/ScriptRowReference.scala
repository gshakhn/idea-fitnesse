package com.gshakhn.idea.idea.fitnesse.scripttable

import com.intellij.psi.{PsiMethod, PsiReferenceBase}

class ScriptRowReference(psiMethod: PsiMethod, element: ScriptRow) extends PsiReferenceBase[ScriptRow](element) {
   override def resolve() = psiMethod

   override def getVariants = Array()
}
