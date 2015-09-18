package fitnesse.idea.fixturemethod

import com.intellij.psi._
import fitnesse.idea.fixtureclass.FixtureClass

trait FixtureMethod extends PsiElement {

  def fixtureClass: Option[FixtureClass]

  def fixtureMethodName: String

  def parameters: List[String]

  def returnType: PsiType

  override def getReference: MethodReference
}
