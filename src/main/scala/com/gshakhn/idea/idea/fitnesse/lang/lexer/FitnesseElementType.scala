package com.gshakhn.idea.idea.fitnesse.lang.lexer

import com.intellij.psi.tree.{TokenSet, IElementType}

class FitnesseElementType(debugName: String) extends IElementType(debugName, com.intellij.openapi.fileTypes.StdFileTypes.PROPERTIES.getLanguage) {
  override def toString = "Fitnesse:" + this.debugName
}

object FitnesseElementType {
  final val WIKI_WORD: IElementType = new FitnesseElementType("WIKI_WORD")
}
