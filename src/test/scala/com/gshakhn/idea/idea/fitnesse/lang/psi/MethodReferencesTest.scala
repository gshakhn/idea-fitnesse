package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement

class MethodReferencesTest {

  class MyMethodReferences(fixtureClass: FixtureClass, methodName: String) extends ASTWrapperPsiElement(null) with MethodReferences {
    override def getFixtureClass: Option[FixtureClass] = Some(fixtureClass)

    override def fixtureMethodName: String = methodName
  }
}
