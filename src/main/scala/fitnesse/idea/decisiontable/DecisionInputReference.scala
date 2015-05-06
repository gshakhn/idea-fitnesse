package fitnesse.idea.decisiontable

import com.intellij.psi.{PsiElement, PsiMethod}
import fitnesse.idea.lang.psi.MethodReference

class DecisionInputReference(psiMethod: PsiMethod, element: DecisionInput) extends MethodReference(psiMethod, element) {

  override def handleElementRename(newElementName: String): PsiElement = {
    if (newElementName.startsWith("set")) {
      super.handleElementRename(newElementName.substring(3))
    } else {
      super.handleElementRename(newElementName)
    }
  }
}
