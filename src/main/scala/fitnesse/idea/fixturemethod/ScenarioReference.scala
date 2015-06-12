package fitnesse.idea.fixturemethod

import com.intellij.psi.PsiReferenceBase
import fitnesse.idea.scripttable.ScenarioName

class ScenarioReference(scenarioName: ScenarioName, element: ScenarioReferences) extends PsiReferenceBase[ScenarioReferences](element) {
  override def resolve() = scenarioName

  override def getVariants = Array()
}
