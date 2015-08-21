package fitnesse.idea.decisiontable

import com.intellij.psi.PsiElement
import fitnesse.idea.fixturemethod.MethodReference

class DecisionInputReference(element: DecisionInput) extends MethodReference(element) {

  override def handleElementRename(newElementName: String): PsiElement = {
    if (newElementName.startsWith("set")) {
      super.handleElementRename(newElementName.substring(3))
    } else {
      super.handleElementRename(newElementName)
    }
  }
}
