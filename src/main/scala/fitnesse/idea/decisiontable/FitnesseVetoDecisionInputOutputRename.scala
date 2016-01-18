package fitnesse.idea.decisiontable

import com.intellij.openapi.util.Condition
import com.intellij.psi.PsiElement
import fitnesse.idea.table.Table

class FitnesseVetoDecisionInputOutputRename extends Condition[PsiElement] {

  // Return true if rename should be veto'ed.
  override def value(t: PsiElement): Boolean = t.isInstanceOf[DecisionInput] || t.isInstanceOf[DecisionOutput] || (!t.isInstanceOf[Table] && t.getParent != null && value(t.getParent))

}
