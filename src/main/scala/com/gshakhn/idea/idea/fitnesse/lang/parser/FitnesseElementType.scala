package com.gshakhn.idea.idea.fitnesse.lang.parser

import com.intellij.psi.tree.{IElementType, IFileElementType}
import com.gshakhn.idea.idea.fitnesse.lang.FitnesseLanguage

class FitnesseElementType(debugName: String) extends IElementType(debugName, FitnesseLanguage.INSTANCE) {
  override def toString = "Fitnesse:" + this.debugName
}

object FitnesseElementType {
  final val FILE = new IFileElementType(FitnesseLanguage.INSTANCE)
  final val TABLE = new FitnesseElementType("TABLE")
  final val ROW = new FitnesseElementType("ROW")
}
