package fitnesse.idea.fixturemethod

import com.intellij.psi.{PsiElement, PsiReferenceBase}
import fitnesse.idea.scripttable.ScenarioName

class ScenarioReference(scenarioName: ScenarioName, element: PsiElement) extends PsiReferenceBase[PsiElement](element) {
  override def resolve = scenarioName

  override def getVariants = Array()
}
