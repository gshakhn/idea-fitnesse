package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.{PsiMethod, PsiReference}
import com.intellij.psi.search.{GlobalSearchScope, PsiShortNamesCache}

abstract class Cell(node: ASTNode) extends ASTWrapperPsiElement(node) {
  def getRow = getParent.asInstanceOf[Row]

}
