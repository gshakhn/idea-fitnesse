package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

class Row(node: ASTNode) extends ASTWrapperPsiElement(node) {

  // Todo: should be part of a TopRow class.
  def getFixtureClass = findChildByClass(classOf[FixtureClass]) match {
    case null => None
    case c => Some(c)
  }

  def getTable = getParent.asInstanceOf[Table]

  def getCells = findChildrenByClass(classOf[Cell])
}
