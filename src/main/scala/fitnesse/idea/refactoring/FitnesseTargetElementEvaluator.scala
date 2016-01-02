package fitnesse.idea.refactoring

import com.intellij.codeInsight.{TargetElementUtilBase, TargetElementEvaluator}
import com.intellij.psi.{PsiReference, PsiElement}
import fitnesse.idea.fixtureclass.FixtureClass

// This class acts as a workaround, to make sure the right element is used to do the rename,
// Somehow, IntelliJ finds it necessary to perform the rename "in place" on a referenced element instead of
// the one under the cursor.
class FitnesseTargetElementEvaluator extends TargetElementEvaluator {
  override def includeSelfInGotoImplementation(psiElement: PsiElement): Boolean = false

  override def getElementByReference(psiReference: PsiReference, flags: Int): PsiElement = {
    if (psiReference.getElement.isInstanceOf[FixtureClass]) {
      psiReference.resolve
    } else if ((flags & TargetElementUtilBase.ELEMENT_NAME_ACCEPTED) != 0) {
      psiReference.getElement
    } else {
      psiReference.resolve
    }
  }
}
