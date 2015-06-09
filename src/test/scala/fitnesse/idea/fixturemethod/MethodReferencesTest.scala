package fitnesse.idea.fixturemethod

import com.intellij.extapi.psi.ASTWrapperPsiElement
import fitnesse.idea.fixtureclass.FixtureClass

class MethodReferencesTest {

  class MyMethodReferences(fixtureClass: FixtureClass, methodName: String) extends ASTWrapperPsiElement(null) with MethodReferences {
    override def getFixtureClass: Option[FixtureClass] = Some(fixtureClass)

    override def fixtureMethodName: String = methodName
  }
}
