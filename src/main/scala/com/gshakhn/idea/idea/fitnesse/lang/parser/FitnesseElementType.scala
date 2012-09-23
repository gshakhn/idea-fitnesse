package com.gshakhn.idea.idea.fitnesse.lang.parser

import com.intellij.psi.tree.IFileElementType
import com.gshakhn.idea.idea.fitnesse.lang.FitnesseLanguage

class FitnesseElementType {}

object FitnesseElementType {
  final val FILE = new IFileElementType(FitnesseLanguage.INSTANCE)
  final val TABLE = new IFileElementType(FitnesseLanguage.INSTANCE)
}
