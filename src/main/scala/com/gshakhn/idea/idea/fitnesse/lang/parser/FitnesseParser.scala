package com.gshakhn.idea.idea.fitnesse.lang.parser

import com.intellij.lang.{PsiBuilder, PsiParser}
import com.intellij.psi.tree.IElementType

class FitnesseParser extends PsiParser {
  def parse(root: IElementType, builder: PsiBuilder) = {
    val rootMarker: PsiBuilder.Marker = builder.mark
    while (!builder.eof()) {
      builder.advanceLexer()
    }
    rootMarker.done(root)
    builder.getTreeBuilt
  }
}
