package fitnesse.idea.fixturemethod

import com.intellij.psi._
import fitnesse.idea.fixtureclass.FixtureClass
import fitnesse.idea.fixturemethod.ReturnType.ReturnType

trait FixtureMethod extends PsiNamedElement {

  def fixtureClass: Option[FixtureClass]

  def fixtureMethodName: String

  def parameters: List[String]

  def returnType: ReturnType

  override def getReference: MethodReference
}
