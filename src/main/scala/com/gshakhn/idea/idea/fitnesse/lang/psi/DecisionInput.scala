package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}
import com.gshakhn.idea.idea.fitnesse.lang.reference.DecisionInputReference

class DecisionInput(node: ASTNode) extends Cell(node) {
  override def getReferences: Array[PsiReference] = {
    val className = getRow.getTable.getFixtureClass.getText
    PsiShortNamesCache.getInstance(getProject)
      .getClassesByName(NameUtils.toJavaClassName(className), GlobalSearchScope.projectScope(getProject))
      .flatMap(_.findMethodsByName("set" + NameUtils.toJavaClassName(node.getText), true))
      .map(new DecisionInputReference(_, this))
  }
}