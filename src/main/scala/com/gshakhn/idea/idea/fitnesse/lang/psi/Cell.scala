package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

abstract class Cell(node: ASTNode) extends ASTWrapperPsiElement(node) {
  def getRow = getParent.asInstanceOf[Row]

  def getFixtureClass = getRow.getTable.getFixtureClass

  def fixtureClassName: Option[String] = getRow.getTable.getFixtureClass match {
    case Some(cls) => cls.fixtureClassName
    case None => None
  }

}
