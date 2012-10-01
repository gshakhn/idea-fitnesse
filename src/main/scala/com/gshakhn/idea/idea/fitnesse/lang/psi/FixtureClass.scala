package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}
import com.gshakhn.idea.idea.fitnesse.lang.reference.FixtureClassReference

class FixtureClass(node: ASTNode) extends ASTWrapperPsiElement(node) {
  override def getReferences = {
    PsiShortNamesCache.getInstance(getProject)
        .getClassesByName(FixtureClass.getClassName(node.getText), GlobalSearchScope.projectScope(getProject))
        .map(new FixtureClassReference(_, this))
  }
}

object FixtureClass {
  def getClassName(gracefulName: String): String = {
    gracefulName.trim
        .replace(".", "")
        .split(' ')
        .map(s => s.head.toUpper + s.tail)
        .reduce((acc, s) => acc + s)
  }
}