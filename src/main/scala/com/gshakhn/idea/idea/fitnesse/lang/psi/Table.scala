package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.intellij.lang.ASTNode
import com.intellij.extapi.psi.ASTWrapperPsiElement

abstract class Table(node: ASTNode) extends ASTWrapperPsiElement(node) {
  def getRows = findChildrenByClass(classOf[Row])

  def getFixtureClass = getRows(0).getFixtureClass
}
