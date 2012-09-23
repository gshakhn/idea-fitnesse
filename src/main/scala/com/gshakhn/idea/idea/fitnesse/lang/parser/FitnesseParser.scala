package com.gshakhn.idea.idea.fitnesse.lang.parser

import com.intellij.lang.{PsiBuilder, PsiParser}
import com.intellij.psi.tree.IElementType
import com.gshakhn.idea.idea.fitnesse.lang.lexer.FitnesseTokenType

class FitnesseParser extends PsiParser {
  def parse(root: IElementType, builder: PsiBuilder) = {
    val rootMarker = builder.mark()
    while (!builder.eof()) {
      if (builder.getTokenType == FitnesseTokenType.TABLE_START) {
        parseTable(builder)
      } else {
        builder.advanceLexer()
      }
    }
    rootMarker.done(root)
    builder.getTreeBuilt
  }

  private def parseTable(builder: PsiBuilder) {
    val start = builder.mark()

    while (builder.getTokenType != FitnesseTokenType.TABLE_END) {
      if (builder.getTokenType == FitnesseTokenType.ROW_START) {
        parseRow(builder)
      } else {
        builder.advanceLexer()
      }
    }

    builder.advanceLexer()
    start.done(FitnesseElementType.TABLE)
  }

  private def parseRow(builder: PsiBuilder) {
    val start = builder.mark()
    while(builder.getTokenType != FitnesseTokenType.ROW_END) {
      builder.advanceLexer()
    }

    builder.advanceLexer()
    start.done(FitnesseElementType.ROW)
  }
}
