package com.gshakhn.idea.idea.fitnesse.decisiontable

import com.intellij.psi.{PsiElement, PsiMethod, PsiReferenceBase}

class DecisionInputReference(psiMethod: PsiMethod, element: DecisionInput) extends PsiReferenceBase[DecisionInput](element) {
  override def resolve() = psiMethod

  override def getVariants = Array()

  override def handleElementRename(newElementName: String): PsiElement = {
    if (newElementName.startsWith("set")) {
      super.handleElementRename(newElementName.substring(3))
    } else {
      super.handleElementRename(newElementName)
    }
  }
}
