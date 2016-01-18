package fitnesse.idea.scripttable

import com.intellij.openapi.util.Condition
import com.intellij.psi.PsiElement
import fitnesse.idea.table.Table

class FitnesseVetoScriptRowRename extends Condition[PsiElement] {

  // Return true if rename should be veto'ed.
  override def value(t: PsiElement): Boolean = t.isInstanceOf[ScriptRow] || (!t.isInstanceOf[Table] && t.getParent != null && value(t.getParent))

}
