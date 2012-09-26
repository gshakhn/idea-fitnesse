package com.gshakhn.idea.idea.fitnesse.lang.parser

import com.intellij.lang.{PsiBuilder, PsiParser}
import com.intellij.psi.tree.IElementType
import com.gshakhn.idea.idea.fitnesse.lang.lexer.FitnesseTokenType
import com.intellij.lang.PsiBuilder.Marker

class FitnesseParser extends PsiParser {
  def parse(root: IElementType, builder: PsiBuilder) = {
    val rootMarker = builder.mark()
    while (!builder.eof()) {
      builder.getTokenType match {
        case FitnesseTokenType.TABLE_START => parseTable(builder)
        case FitnesseTokenType.PERIOD => {
          builder.lookAhead(1) match {
            case FitnesseTokenType.WIKI_WORD => parseLink(builder, WikiLinkElementType.ABSOLUTE_WIKI_LINK)
            case _ => builder.advanceLexer()
          }
        }
        case FitnesseTokenType.WIKI_WORD => parseLink(builder, WikiLinkElementType.RELATIVE_WIKI_LINK)
        case _ => builder.advanceLexer()
      }
    }

    rootMarker.done(root)
    builder.getTreeBuilt
  }

  private def parseLink(builder: PsiBuilder, linkType: WikiLinkElementType) {
    val start = builder.mark()
    while (!builder.eof() && (builder.getTokenType == FitnesseTokenType.WIKI_WORD || builder.getTokenType == FitnesseTokenType.PERIOD)) {
      builder.advanceLexer()
    }

    start.done(linkType)
  }

  private def parseTable(builder: PsiBuilder) {
    val start = builder.mark()

    while (!builder.eof() && builder.getTokenType != FitnesseTokenType.TABLE_END) {
      builder.getTokenType match {
        case FitnesseTokenType.ROW_START => parseRow(builder)
        case _ => builder.advanceLexer()
      }
    }

    start.done(FitnesseElementType.TABLE)
  }

  private def parseRow(builder: PsiBuilder) {
    val start = builder.mark()
    while(!builder.eof() && builder.getTokenType != FitnesseTokenType.ROW_END) {
      builder.advanceLexer()
    }

    start.done(FitnesseElementType.ROW)
  }
}
