package fitnesse.idea.fixturemethod

import com.intellij.extapi.psi.ASTWrapperPsiElement
import fitnesse.idea.fixtureclass.FixtureClass

class MethodReferencesTest {

  class MyFixtureMethods(fixtureClass: FixtureClass, methodName: String) extends ASTWrapperPsiElement(null) with FixtureMethod {
    override def getFixtureClass: Option[FixtureClass] = Some(fixtureClass)

    override def fixtureMethodName: String = methodName
  }
}
