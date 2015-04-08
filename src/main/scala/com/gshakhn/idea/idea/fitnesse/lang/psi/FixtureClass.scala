package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.gshakhn.idea.idea.fitnesse.lang.reference.FixtureClassReference
import com.intellij.lang.ASTNode
import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}

class FixtureClass(node: ASTNode) extends Cell(node) {

  def fixtureClassName = Disgracer.disgraceClassName(node.getText)

  override def getReferences = {
    PsiShortNamesCache.getInstance(getProject)
        .getClassesByName(fixtureClassName, GlobalSearchScope.projectScope(getProject))
        .map(new FixtureClassReference(_, this))
  }
}