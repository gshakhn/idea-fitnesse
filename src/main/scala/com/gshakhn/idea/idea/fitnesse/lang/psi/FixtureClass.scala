package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.gshakhn.idea.idea.fitnesse.lang.manipulator.FixtureClassManipulator
import com.gshakhn.idea.idea.fitnesse.lang.reference.FixtureClassReference
import com.intellij.lang.ASTNode
import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}
import com.intellij.psi.{PsiElement, PsiNamedElement}
import fitnesse.testsystems.slim.tables.Disgracer.disgraceClassName

class FixtureClass(node: ASTNode) extends Cell(node) with PsiNamedElement {

  override def fixtureClassName: Option[String] = disgraceClassName(node.getText.trim) match {
    case "" => None
    case className => Some(className)
  }

  override def getReferences = {
    fixtureClassName match {
      case Some(className) =>
        PsiShortNamesCache.getInstance(getProject)
          .getClassesByName(className, GlobalSearchScope.projectScope(getProject))
          .map(new FixtureClassReference(_, this))
      case None => Array()
    }
  }

  //  override def getName = fixtureClassName.get

  override def setName(s: String): PsiElement = FixtureClassManipulator.createFixtureClass(getProject, s)
}