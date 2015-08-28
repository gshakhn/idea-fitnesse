package fitnesse.idea.decisiontable

import com.intellij.psi.PsiElement
import fitnesse.idea.fixturemethod.{MethodOrScenarioArgumentReference, MethodReference}

class DecisionInputReference(element: DecisionInput) extends MethodOrScenarioArgumentReference(element) {

  override def handleElementRename(newElementName: String): PsiElement = {
    // TODO: regrace??
    if (newElementName.startsWith("set")) {
      super.handleElementRename(newElementName.substring(3))
    } else {
      super.handleElementRename(newElementName)
    }
  }
}
