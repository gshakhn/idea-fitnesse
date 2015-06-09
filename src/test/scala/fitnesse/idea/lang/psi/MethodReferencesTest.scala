package fitnesse.idea.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import fitnesse.idea.fixtureclass.FixtureClass
import fitnesse.idea.fixturemethod.MethodReferences

class MethodReferencesTest {

  class MyMethodReferences(fixtureClass: FixtureClass, methodName: String) extends ASTWrapperPsiElement(null) with MethodReferences {
    override def getFixtureClass: Option[FixtureClass] = Some(fixtureClass)

    override def fixtureMethodName: String = methodName
  }
}
