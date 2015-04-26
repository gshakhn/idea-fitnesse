package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

abstract class Cell(node: ASTNode) extends ASTWrapperPsiElement(node) {
  def getRow = getParent.asInstanceOf[Row]

  def fixtureClassName: String = getRow.getTable.getFixtureClass.fixtureClassName

}
