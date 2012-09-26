package com.gshakhn.idea.idea.fitnesse.lang.parser

import com.intellij.lang.{PsiBuilder, PsiParser}
import com.intellij.psi.tree.IElementType
import com.gshakhn.idea.idea.fitnesse.lang.lexer.FitnesseTokenType
import com.intellij.lang.PsiBuilder.Marker

class FitnesseParser extends PsiParser {
  def parse(root: IElementType, builder: PsiBuilder) = {
    val rootMarker = builder.mark()
    while (!builder.eof()) {
      if (builder.getTokenType == FitnesseTokenType.TABLE_START) {
        parseTable(builder)
      } else if (builder.getTokenType == FitnesseTokenType.PERIOD) {
        val linkStart = builder.mark()
        builder.advanceLexer()
        if (builder.getTokenType == FitnesseTokenType.WIKI_WORD) {
          parseLink(builder, linkStart, WikiLinkElementType.ABSOLUTE_WIKI_LINK)
        } else {
          linkStart.drop()
        }
      } else if (builder.getTokenType == FitnesseTokenType.WIKI_WORD) {
        parseLink(builder, builder.mark(), WikiLinkElementType.RELATIVE_WIKI_LINK)
      } else {
        builder.advanceLexer()
      }
    }
    rootMarker.done(root)
    builder.getTreeBuilt
  }

  private def parseLink(builder: PsiBuilder, start: Marker, linkType: WikiLinkElementType) {
    while (!builder.eof() && (builder.getTokenType == FitnesseTokenType.WIKI_WORD || builder.getTokenType == FitnesseTokenType.PERIOD)) {
      builder.advanceLexer()
    }

    start.done(linkType)
  }

  private def parseTable(builder: PsiBuilder) {
    val start = builder.mark()

    while (!builder.eof() && builder.getTokenType != FitnesseTokenType.TABLE_END) {
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
    while(!builder.eof() && builder.getTokenType != FitnesseTokenType.ROW_END) {
      builder.advanceLexer()
    }

    builder.advanceLexer()
    start.done(FitnesseElementType.ROW)
  }
}
