package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.gshakhn.idea.idea.fitnesse.lang.reference.DecisionInputReference
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}

class DecisionInput(node: ASTNode) extends Cell(node) {

  def className = Disgracer.disgraceClassName(getRow.getTable.getFixtureClass.getText)

  def methodName = "set" + Disgracer.disgraceClassName(node.getText)

  override def getReferences: Array[PsiReference] = {
    PsiShortNamesCache.getInstance(getProject)
      .getClassesByName(className, GlobalSearchScope.projectScope(getProject))
      .flatMap(_.findMethodsByName(methodName, true))
      .map(new DecisionInputReference(_, this))
  }
}