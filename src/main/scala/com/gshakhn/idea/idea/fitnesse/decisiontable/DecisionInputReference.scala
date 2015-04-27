package com.gshakhn.idea.idea.fitnesse.decisiontable

import com.gshakhn.idea.idea.fitnesse.lang.psi.MethodReference
import com.intellij.psi.{PsiElement, PsiMethod}

class DecisionInputReference(psiMethod: PsiMethod, element: DecisionInput) extends MethodReference(psiMethod, element) {

  override def handleElementRename(newElementName: String): PsiElement = {
    if (newElementName.startsWith("set")) {
      super.handleElementRename(newElementName.substring(3))
    } else {
      super.handleElementRename(newElementName)
    }
  }
}
