package com.gshakhn.idea.idea.fitnesse.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.gshakhn.idea.idea.fitnesse.lang.reference.WikiLinkReference

class WikiLink(node: ASTNode) extends ASTWrapperPsiElement(node) {
  override def getReference = new WikiLinkReference(this)
}
