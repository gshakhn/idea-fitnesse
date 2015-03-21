package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.gshakhn.idea.idea.fitnesse.lang.reference.FixtureClassReference
import com.intellij.lang.ASTNode
import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}

class FixtureClass(node: ASTNode) extends Cell(node) {
  override def getReferences = {
    PsiShortNamesCache.getInstance(getProject)
        .getClassesByName(NameUtils.toJavaClassName(node.getText), GlobalSearchScope.projectScope(getProject))
        .map(new FixtureClassReference(_, this))
  }
}